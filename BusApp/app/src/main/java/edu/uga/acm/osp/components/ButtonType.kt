package edu.uga.acm.osp.components

/**
 * Specifies possible types of buttons, determining their colors.
 */
enum class ButtonType {
    /**
     * Important or affirmative actions. The "main" or "expected" button for users to press among
     * other options.
     */
    PRIMARY,

    /**
     * Secondary or negative actions. The alternate or less-used option when other buttons are
     * present.
     */
    SECONDARY
}