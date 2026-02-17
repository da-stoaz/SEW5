package net.stoaz.a.persistance;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

public class FragmentSettingsDisplay extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences preferences;

    private TextView tvSignatureValue;
    private TextView tvReplyValue;
    private TextView tvSyncValue;
    private TextView tvAttachmentValue;
    private TextView tvDisableBrainValue;

    public FragmentSettingsDisplay() {
        super(R.layout.fragment_settings_display);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        tvSignatureValue = view.findViewById(R.id.tv_signature_value);
        tvReplyValue = view.findViewById(R.id.tv_reply_value);
        tvSyncValue = view.findViewById(R.id.tv_sync_value);
        tvAttachmentValue = view.findViewById(R.id.tv_attachment_value);
        tvDisableBrainValue = view.findViewById(R.id.tv_disable_brain_value);

        updateUi();
    }

    @Override
    public void onResume() {
        super.onResume();
        preferences.registerOnSharedPreferenceChangeListener(this);
        updateUi();
    }

    @Override
    public void onPause() {
        preferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, @Nullable String key) {
        updateUi();
    }

    private void updateUi() {
        String signature = preferences.getString("signature", "");
        String reply = preferences.getString("reply", "reply");
        boolean sync = preferences.getBoolean("sync", false);
        boolean attachment = preferences.getBoolean("attachment", false);
        boolean disableBrain = preferences.getBoolean("disable_brain", false);

        tvSignatureValue.setText(valueOrPlaceholder(signature));
        tvReplyValue.setText(resolveReplyLabel(reply));
        tvSyncValue.setText(booleanLabel(sync));
        tvDisableBrainValue.setText(booleanLabel(disableBrain));

        if (!sync) {
            tvAttachmentValue.setText(getString(R.string.settings_value_attachment_disabled_by_dependency));
        } else {
            tvAttachmentValue.setText(booleanLabel(attachment));
        }
    }

    private String resolveReplyLabel(String value) {
        String[] values = getResources().getStringArray(R.array.reply_values);
        String[] entries = getResources().getStringArray(R.array.reply_entries);

        for (int i = 0; i < values.length; i++) {
            if (values[i].equals(value)) {
                return entries[i];
            }
        }

        return valueOrPlaceholder(value);
    }

    private String valueOrPlaceholder(String value) {
        if (value == null || value.trim().isEmpty()) {
            return getString(R.string.settings_value_not_set);
        }
        return value;
    }

    private String booleanLabel(boolean value) {
        return value
                ? getString(R.string.settings_value_on)
                : getString(R.string.settings_value_off);
    }
}
