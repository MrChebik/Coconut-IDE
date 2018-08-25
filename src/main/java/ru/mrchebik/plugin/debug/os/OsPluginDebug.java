package ru.mrchebik.plugin.debug.os;

public abstract class OsPluginDebug implements OsPluginDebugWrapper {
    /**
     * Return an type of Os.
     * <p>
     * 1. Linux
     * 2. Windows
     * 3. Mac
     * 4. Solaris
     * 5. Unsupported
     *
     * @param os
     * @return
     */
    public static OsPluginDebug getOs(OsPluginDebug... os) {
        String osProp = System.getProperty("os.name");

        try {
            return "Linux".contains(osProp) ?
                    os[0]
                    :
                    "Windows".contains(osProp) ?
                            os[1]
                            :
                            "Mac".contains(osProp) ?
                                    os[2]
                                    :
                                    "Solaris".contains(osProp) ?
                                            os[3]
                                            :
                                            os[4];
        } catch (IndexOutOfBoundsException ignored) {
            return new OsUnsupported();
        }
    }

    @Override
    public String[] getCommand() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object computeOutput(StringBuilder input) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSupported() {
        return true;
    }
}
