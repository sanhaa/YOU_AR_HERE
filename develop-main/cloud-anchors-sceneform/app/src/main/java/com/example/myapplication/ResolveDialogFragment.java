/*
 * Copyright 2018 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import android.widget.LinearLayout.LayoutParams;

import androidx.fragment.app.DialogFragment;

public class ResolveDialogFragment extends DialogFragment {
  interface OkListener {
    void onOkPressed(String dialogValue);
  }
  private OkListener okListener;
  private EditText shortCodeField;

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
    shortCodeField = new EditText(context);
    shortCodeField.setInputType(InputType.TYPE_CLASS_NUMBER);
    shortCodeField.setLayoutParams(
            new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    shortCodeField.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
    layout.addView(shortCodeField);
    layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    return layout;
  }
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder
            .setView(getDialogLayout())
            .setTitle("Resolve Anchor")
            .setPositiveButton(
                    "OK",
                    (dialog, which) -> {
                      Editable shortCodeText = shortCodeField.getText();
                      if (okListener != null && shortCodeText != null && shortCodeText.length() > 0) {
                        // Invoke the callback with the current checked item.
                        okListener.onOkPressed(shortCodeText.toString());
                      }
                    })
            .setNegativeButton("Cancel", (dialog, which) -> {});
    return builder.create();
  }
}

/** A DialogFragment for the Resolve Dialog Box. */

//public class ResolveDialogFragment extends DialogFragment {
//
//  // The maximum number of characters that can be entered in the EditText.
//  private static final int MAX_FIELD_LENGTH = 6;
//
//  /** Functional interface for getting the value entered in this DialogFragment. */
//  public interface OkListener {
//    /**
//     * This method is called by the dialog box when its OK button is pressed.
//     *
//     * @param dialogValue the long value from the dialog box
//     */
//    void onOkPressed(int dialogValue);
//  }
//
//  public static ResolveDialogFragment createWithOkListener(OkListener listener) {
//    ResolveDialogFragment frag = new ResolveDialogFragment();
//    frag.okListener = listener;
//    return frag;
//  }
//
//  private EditText shortCodeField;
//  private OkListener okListener;
//
//  @Override
//  public Dialog onCreateDialog(Bundle savedInstanceState) {
//    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//    builder
//        .setView(createDialogLayout())
//        .setTitle("Resolve Anchor")
//        .setPositiveButton("Resolve", (dialog, which) -> onResolvePressed())
//        .setNegativeButton("Cancel", (dialog, which) -> {});
//    return builder.create();
//  }
//
//  private LinearLayout createDialogLayout() {
//    Context context = getContext();
//    LinearLayout layout = new LinearLayout(context);
//    shortCodeField = new EditText(context);
//    // Only allow numeric input.
//    shortCodeField.setInputType(InputType.TYPE_CLASS_NUMBER);
//    shortCodeField.setLayoutParams(
//        new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//    // Set a max length for the input text to avoid overflows when parsing.
//    shortCodeField.setFilters(new InputFilter[] {new InputFilter.LengthFilter(MAX_FIELD_LENGTH)});
//    layout.addView(shortCodeField);
//    layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//    return layout;
//  }
//
//  private void onResolvePressed() {
//    Editable roomCodeText = shortCodeField.getText();
//    if (okListener != null && roomCodeText != null && roomCodeText.length() > 0) {
//      int longVal = Integer.parseInt(roomCodeText.toString());
//      okListener.onOkPressed(longVal);
//    }
//  }
//}
