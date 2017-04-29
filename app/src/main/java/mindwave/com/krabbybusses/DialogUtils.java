package mindwave.com.krabbybusses;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

/**
 * Utilities for creating dialogs.
 *
 * @author Jimmy Shih
 */
public class DialogUtils {

    private DialogUtils() {}

    /**
     * Creates a confirmation dialog.
     *
     * @param context the context
     * @param messageId the message id
     * @param okListener the listener when OK is clicked
     */
    public static Dialog createConfirmationDialog(
            Context context, int messageId, DialogInterface.OnClickListener okListener) {
        return createConfirmationDialog(context, messageId, null, okListener, null);
    }

    /**
     * Creates a confirmation dialog.
     *
     * @param context the context
     * @param messageId the messageId
     * @param view the view
     * @param okListener the listener when OK is clicked
     * @param cancelListener the listener when cancel is clicked
     */
    public static Dialog createConfirmationDialog(
            Context context, int messageId, View view, DialogInterface.OnClickListener okListener,
            DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setCancelable(true)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setNegativeButton(android.R.string.cancel, cancelListener)
                .setPositiveButton(android.R.string.ok, okListener)
                .setTitle("Confirm");
        if (messageId != -1) {
            builder.setMessage(messageId);
        }
        if (view != null) {
            builder.setView(view);
        }

        return builder.create();
    }

    /**
     * Creates a spinner progress dialog.
     *
     * @param context the context
     * @param messageId the progress message id
     * @param onCancelListener the cancel listener
     */
    public static ProgressDialog createSpinnerProgressDialog(
            Context context, int messageId, DialogInterface.OnCancelListener onCancelListener) {
        return createProgressDialog(true, context, messageId, onCancelListener);
    }

    /**
     * Creates a horizontal progress dialog.
     *
     * @param context the context
     * @param messageId the progress message id
     * @param onCancelListener the cancel listener
     * @param formatArgs the format arguments for the messageId
     */
    public static ProgressDialog createHorizontalProgressDialog(Context context, int messageId,
                                                                DialogInterface.OnCancelListener onCancelListener, Object... formatArgs) {
        return createProgressDialog(false, context, messageId, onCancelListener, formatArgs);
    }

    /**
     * Creates a progress dialog.
     *
     * @param spinner true to use the spinner style
     * @param context the context
     * @param messageId the progress message id
     * @param onCancelListener the cancel listener
     * @param formatArgs the format arguments for the message id
     */
    private static ProgressDialog createProgressDialog(boolean spinner, Context context,
                                                       int messageId, DialogInterface.OnCancelListener onCancelListener, Object... formatArgs) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIcon(android.R.drawable.ic_dialog_info);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(context.getString(messageId, formatArgs));
        progressDialog.setOnCancelListener(onCancelListener);
        progressDialog.setProgressStyle(spinner ? ProgressDialog.STYLE_SPINNER
                : ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Drawing Map");
        return progressDialog;
    }

    public static Dialog createProgressDialog(Navigation_Maps activity, String string) {
        // TODO Auto-generated method stub
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIcon(android.R.drawable.ic_dialog_info);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle("Drawing Map");
        return progressDialog;
    }
}