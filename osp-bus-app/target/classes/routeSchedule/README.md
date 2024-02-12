## About Route Schedules:
### What Are Route Schedules?
A single `RouteSchedule` object contains all the information about a particular `Route`'s operating periods.
Every `RouteSchedule` object has an Array of `OperatingWindow`s, each of which contains an array of `Session`s (either of
`CustomSession` or `PredefinedSession` type) and a `DailySchedule`. Every `OperatingDate` contains a start and end `LocalDate`,
which, when contained in the `OperatingDate`s Array inside a `Session`, specifies what dates the given `DailySchedule` is in
effect. Every `OperatingTime` in each `DayOfWeek`'s Array contains a start and end `LocalTime` at which the route is deemed to
be "operating" within.

This structure allows us to define a wide variety of schedules in a logical manner, with minimal need to redundantly
define information. As a brief overview, the structure looks like...

| Class           | Fields                                 |
|-----------------|----------------------------------------|
| RouteSchedule   | `OperatingWindow[]`                    |
| OperatingWindow | `Session[]`, `DailySchedule`           |
| Session         | `OperatingDate[]`                      |
| DailySchedule   | `HashMap<DayOfWeek`, `OperatingTime[]` |
| OperatingDate   | Start and end `LocalDate`s             |
| OperatingTime   | Start and end `LocalTime`s             |

`RouteSchedule`s have the unique ability to be instantiated from an encoded `String` derived from a database or
passed as an argument through `RouteSchedule.decode(encodedRouteScheduleString)`. Additionally, every instance
of a `RouteSchedule` can be re-encoded into a database-storeable and decodable `String` through `routeSchedule.encode()`.

This approach was chosen over traditional serialization to facilitate schedule writing/editing, minimize `String` size,
and make encoded schedules that are stored in the database quick and easy to understand and modify.

---

### Encoding Format:
Below are a few examples of valid formats demonstrating `RouteSchedule`'s ability to efficiently represent a variety of
schedule types:

>**Multiple operating windows...**\
`MMDDYY{?:HH:II-HH:II,HH:II-;?:-HH:II,HH:II-;??:-;?:-HH:II;}|MMDDYY,MMDDYY{?:HH:II-HH:II,HH:II-;?:-HH:II,HH:II-;??:-;?:-HH:II;}`

>**Single operating window with a single custom session and different daily times...**\
`MMDDYY-MMDDYY,MMDDYY-{?:HH:II-HH:II,HH:II-;?:-HH:II,HH:II-;??:-;?:-HH:II;}`

>**Single operating window with a single predefined session and constant daily times...**\
`${HH:II-HH:II,HH:II-HH:II}`

>**Single operating window with multiple sessions and different daily times...**\
`-MMDDYY,MMDDYY-MMDDYY,MMDDYY;${?:HH:II-HH:II,HH:II-;?:-HH:II,HH:II-;??:-;?:-HH:II;}`

>**Single operating window with a multiple sessions and constant daily times...**\
`-MMDDYY,MMDDYY-MMDDYY,MMDDYY;${HH:II-HH:II,HH:II-HH:II}`

>**Single operating window with continuous operation and different daily times...**\
`{?:HH:II-HH:II,HH:II-;?:-HH:II,HH:II-;??:-;?:-HH:II;}`

>**Single operating window with continuous operation and constant daily times...**\
`{?-?:HH:II-HH:II;}`

* Where ? represents a day indicator (U=Sunday, M=Monday, T=Tuesday, W=Wednesday R=Thursday, F=Friday, S=Saturday)
* Where $ represents a predefined session indicator (as defined in PredefinedSession)
* Where HH represents an hour integer from 00-23 in 24-hour format, with a leading 0 included for single-digit hours
* Where II represents a minute integer from 00-59, with a leading 0 included for single-digit minutes
* Where MM represents a month integer from 01-12, with a leading 0 included for single-digit months
* Where DD represents a day integer from 01-31, with a leading 0 included for single-digit days (day must be valid for
given month and year)
* Where YY represents a year integer from 00-99 (as in 20YY), with a leading 0 included for single-digit years

---

### Encoding Rules:
1. At least one operating window must exist.
2. 0-unlimited operating dates can exist (0 indicates indefinite operation with the DailySchedule that follows).
3. Multiple operating dates must be ordered chronologically, by the start date if a range.
4. At least one day of the week must be defined in a daily schedule UNLESS only a single parent date is specified, in which
only times are necessary (no ?: needed).
5. Multiple day indicators must be ordered chronologically in sets AND by the first day indicator in the daily schedule.
6. Regardless of position in sequence, every list of times must end in ; when a day or days is/are specified.
7. 0-unlimited operating times can exist per day.
8. Multiple operating times must be ordered chronologically by the start time.
9. Operating times with pre continuity must always be first in sequence. Operating times with post continuity must be last.

**Notes:**
* Encoded operating windows exist between the start of the encoded string, any '|'s and the end of the encoded string.
* The daily schedule for a parent Session exists between '{' and '}'.
* A lack of day of week indicators (UMTWRFS) indicates the contained operating times apply to every day included in the
parent operating date.
* Encoded operating times exist between '{' and '}' if they apply to all days included in the parent operating date or, if
days of the week have unique operating times, between ':' and ';', as separated by ',' if multiple exist

---

### Encoding Examples:
Every example below is a valid encoding. Feel free to use `System.out.println(RouteSchedule.decode(encodedString).toString())`
to interpret any:

>`-121223{U:-2030;M-F:0830-2230;S:0830-;}`\
>**_From the date of instantiation until 12/12/23..._**\
>The route operates continuously from Saturday at 8:30am to Sunday at 8:30pm EST;\
And from Monday to Friday from 8:30am to 10:30pm EST.

>`012524-{U:1725-;M:-0230,0830-1230,1330-1400,1740-;TW:-;R:-0830;}`\
>**_From 1/25/24 onwards..._**\
>The route operates continuously from Sunday at 5:25pm to Monday at 2:30am EST;\
>And on Monday from 8:30am to 12:30pm, 1:30pm to 2:00pm, and from 5:40pm Monday to 8:30am Thursday continuously.

>`-012324,012524-022724,030124,030324-{U:1725-;M:-0230,0830-1230,1330-1400,1740-;TW:-;R:-0830;}`\
>**_From the date of instantiation until 1/23/24, from 1/25/24 to 2/27/24, on 3/1/24, and from 3/3/24 onwards..._**\
>The route operates continuously from Sunday at 5:25pm to Monday at 2:30am EST;\
>And on Monday at 8:30am to 12:30pm, 1:30pm to 2:00pm, and from 5:40pm Monday to 8:30am Thursday continuously.

>`121223{0830-2230}`\
>**_On 12/12/23..._**\
>The route operates from 8:30am to 8:30pm EST.

>`{M:0830-1230;}`\
>**_For the indefinite future..._**\
>The route operates on Mondays from 8:30am to 12:30pm.

>`121223;s{M:0830-1230;}`\
>**_On 12/12/23 and during the predefined session 's'..._**\
>The route operates on Mondays from 8:30am to 12:30pm.