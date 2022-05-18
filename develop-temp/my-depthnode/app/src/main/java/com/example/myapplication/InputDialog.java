package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.fragment.app.DialogFragment;

public class InputDialog extends DialogFragment {
    interface OkListener {
        void onOkPressed(String dialogValue);
    }
    private OkListener okListener;
    private EditText messageField;

    /** Sets a listener that is invoked when the OK button on this dialog is pressed. */
    void setOkListener(OkListener okListener) {
        this.okListener = okListener;
    }

    /**
     * Creates a simple layout for the dialog. This contains a single user-editable text field whose
     * input type is retricted to numbers only, for simplicity.
     */
    private LinearLayout getDialogLayout() {
        Context context = getContext();
        LinearLayout layout = new LinearLayout(context);
        messageField = new EditText(context);
        messageField.setInputType(InputType.TYPE_CLASS_TEXT);
        messageField.setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        messageField.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
        layout.addView(messageField);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return layout;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setView(getDialogLayout())
                .setTitle("Leave message ... ")
                .setPositiveButton(
                        "OK",
                        (dialog, which) -> {
                            Editable messageText = messageField.getText();
                            if (okListener != null && messageText != null && messageText.length() > 0) {
                                // Invoke the callback with the current checked item.
                                okListener.onOkPressed(messageText.toString());
                            }
                        })
                .setNegativeButton("Cancel", (dialog, which) -> {});
        return builder.create();
    }
}