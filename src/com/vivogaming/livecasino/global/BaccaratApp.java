package com.vivogaming.livecasino.global;

import android.app.Application;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: Vasja
 * Date: 07.11.13
 * Time: 11:21
 */
public final class BaccaratApp extends Application {
    private Thread.UncaughtExceptionHandler androidDefaultUEH;

    private Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
        @Override
        public final void uncaughtException(final Thread _thread, final Throwable _throwable) {
            try {
                printErrorLogToFile(_throwable, "uncaughtException");
            } finally {
                androidDefaultUEH.uncaughtException(_thread, _throwable);
            }
        }
    };

    @Override
    public final void onCreate() {
        super.onCreate();

        androidDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(handler);
    }

    private static final String getStackTraceFromThrowableAsString(final Throwable _throwable) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final PrintStream ps = new PrintStream(baos);
        _throwable.printStackTrace(ps);
        ps.close();
        return baos.toString();
    }

    private static final String getHeaderStringWithTimestamp(final String _description) {
        final String pattern = "dd/MM/yyyy-HH:mm:ss:SS";
        final SimpleDateFormat format = new SimpleDateFormat(pattern);
        final String header =
                "---------------------------------------------------------------\n" +
                _description + "\n" +
                format.format(new Date()) + "\n" +
                "---------------------------------------------------------------\n";
        return header;
    }

    public static final void printErrorLogToFile(final Throwable _throwable, final String _description) {
        final String stacktrace = getStackTraceFromThrowableAsString(_throwable);
        final String header = getHeaderStringWithTimestamp(_description);

        try {
            final FileOutputStream fos = new FileOutputStream(
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/baccarat_log.txt", true);
            fos.write(header.getBytes());
            fos.write(stacktrace.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
