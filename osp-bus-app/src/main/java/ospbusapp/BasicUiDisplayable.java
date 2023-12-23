package ospbusapp;

/**
 * Enables classes to be displayed in basic UI components on the frontend
 */
public interface BasicUiDisplayable {
    /**
     * Provides appropriate info to be displayed as the header for the given object
     *
     * @return a {@code String} suitable for use as a header
     */
    public String getHeader();

    /**
     * Provides appropriate info to be displayed as the subheader for the given object
     *
     * @return a {@code String} suitable for use as a subheader
     */
    public String getSubHeader();

    /**
     * Provides appropriate info to be displayed as the primary context for the given object
     *
     * @return a {@code String} suitable for use as primary context
     */
    public String getContext1();

    /**
     * Provides appropriate info to be displayed as the secondary context for the given object
     *
     * @return a {@code String} suitable for use as secondary context
     */
    public String getContext2();
}