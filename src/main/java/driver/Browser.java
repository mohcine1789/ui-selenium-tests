package driver;

import org.apache.commons.lang3.StringUtils;

import java.util.EnumSet;

public enum Browser {
    CHROME,
    FIREFOX;

    /**
     * Gets browser by name.
     *
     * @param name the name
     * @return the browser by name
     */
    public static Browser getBrowserByName(String name) {
        return EnumSet.allOf(Browser.class).stream().
                filter(browsers -> StringUtils.equalsIgnoreCase(browsers.name(), name))
                .findFirst().orElseThrow(() -> new NoSuchFieldError(String.format("No such browser found %s", name)));
    }
}