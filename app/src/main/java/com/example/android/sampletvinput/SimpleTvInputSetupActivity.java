/*
 * Copyright 2014 Google Inc. All rights reserved.
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

package com.example.android.sampletvinput;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.media.tv.TvContract;
import android.media.tv.TvInputInfo;
import android.net.Uri;
import android.os.Bundle;

/**
 * The setup activity for SimpleTvInputService.
 */
public class SimpleTvInputSetupActivity extends Activity {
    private static final String CHANNEL_1_NUMBER = "1-1";
    private static final String CHANNEL_1_NAME = "Bunny - Low Resolution";
    private static final int CHANNEL_1_ORIG_NETWORK_ID = 0;
    private static final int CHANNEL_1_TRANSPORT_STREAM_ID = 0;
    public static final int CHANNEL_1_SERVICE_ID = 1;

    private static final String CHANNEL_2_NUMBER = "1-2";
    private static final String CHANNEL_2_NAME = "Bunny - High Resolution";
    private static final int CHANNEL_2_ORIG_NETWORK_ID = 0;
    private static final int CHANNEL_2_TRANSPORT_STREAM_ID = 0;
    public static final int CHANNEL_2_SERVICE_ID = 2;

    private String mInputId = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInputId = getIntent().getStringExtra(TvInputInfo.EXTRA_INPUT_ID);

        DialogFragment newFragment = new MyAlertDialogFragment();
        newFragment.show(getFragmentManager(), "dialog");
    }

    private void registerChannels() {
        // Check if we already registered channels.
        Uri uri = TvContract.buildChannelsUriForInput(mInputId);
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                return;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        ContentValues values = new ContentValues();
        values.put(TvContract.Channels.COLUMN_INPUT_ID, mInputId);

        // Register channel 1-1.
        values.put(TvContract.Channels.COLUMN_DISPLAY_NUMBER, CHANNEL_1_NUMBER);
        values.put(TvContract.Channels.COLUMN_DISPLAY_NAME, CHANNEL_1_NAME);
        values.put(TvContract.Channels.COLUMN_ORIGINAL_NETWORK_ID, CHANNEL_1_ORIG_NETWORK_ID);
        values.put(TvContract.Channels.COLUMN_TRANSPORT_STREAM_ID, CHANNEL_1_TRANSPORT_STREAM_ID);
        values.put(TvContract.Channels.COLUMN_SERVICE_ID, CHANNEL_1_SERVICE_ID);
        getContentResolver().insert(TvContract.Channels.CONTENT_URI, values);

        // Register channel 1-2.
        values.put(TvContract.Channels.COLUMN_DISPLAY_NUMBER, CHANNEL_2_NUMBER);
        values.put(TvContract.Channels.COLUMN_DISPLAY_NAME, CHANNEL_2_NAME);
        values.put(TvContract.Channels.COLUMN_ORIGINAL_NETWORK_ID, CHANNEL_2_ORIG_NETWORK_ID);
        values.put(TvContract.Channels.COLUMN_TRANSPORT_STREAM_ID, CHANNEL_2_TRANSPORT_STREAM_ID);
        values.put(TvContract.Channels.COLUMN_SERVICE_ID, CHANNEL_2_SERVICE_ID);
        getContentResolver().insert(TvContract.Channels.CONTENT_URI, values);
    }

    public static class MyAlertDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.register_channels)
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ((SimpleTvInputSetupActivity) getActivity()).registerChannels();
                                    getActivity().finish();
                                }
                            }
                    )
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    getActivity().finish();
                                }
                            }
                    )
                    .create();
        }
    }
}
