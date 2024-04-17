package edu.uga.acm.osp.data.display;

/**
 * Enables implementing classes to be displayed as list elements on the frontend
 */
public interface ListItemData {
    /**
     * Provides appropriate info to be displayed as the header for the given object
     *
     * @return a {@code String} suitable for use as a header
     */
    public String listItemHeader(UiContext ctx);

    /**
     * Provides appropriate info to be displayed as the subheader for the given object
     *
     * @return a {@code String} suitable for use as a subheader
     */
    public String listItemSubHeader(UiContext ctx);

    /**
     * Provides appropriate info to be displayed as the primary context for the given object
     *
     * @return a {@code String} suitable for use as primary context
     */
    public String listItemContext1(UiContext ctx);

    /**
     * Provides appropriate info to be displayed as the secondary context for the given object
     *
     * @return a {@code String} suitable for use as secondary context
     */
    public String listItemContext2(UiContext ctx);
}