/*
 * Copyright (C) 2015 The Dirty Unicorns Project
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

package com.android.settings.rr;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.SwitchPreference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.provider.SearchIndexableResource;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.util.Helpers;
import com.android.internal.logging.MetricsLogger;
import net.margaritov.preference.colorpicker.ColorPickerPreference;


import java.util.List;
import java.util.ArrayList;

public class StatusBarColors extends SettingsPreferenceFragment implements OnPreferenceChangeListener ,Indexable {

    private static final String PREF_SIGNAL =
            "network_icons_signal_color";
     private static final String PREF_NO_SIM =
             "network_icons_no_sim_color";
     private static final String PREF_AIRPLANE_MODE =
             "network_icons_airplane_mode_color";
     private static final String PREF_COLOR =
             "notification_icons_color";
     private static final String PREF_STATUS =
             "network_status_icons_status_color";

     private static final String BATTERY_ICON =
             "battery_icon_color";
     private static final String BATTERY_TEXT =
             "battery_text_color";

     private static final String ENABLE_COLORS = "statusbar_color_switch";
     private static final String ENABLE ="enable_status_colors";
 
 
     private static final int WHITE                  = 0xffffffff;
     private static final int HOLO_BLUE_LIGHT        = 0xff33b5e5;
     private static final int RED_500                = 0xfff44336;
     private static final int BLACK_TRANSLUCENT      = 0x99000000;
     private static final int RED_900_TRANSLUCENT    = 0x99b71c1c;
 
     private static final int MENU_RESET = Menu.FIRST;
     private static final int DLG_RESET  = 0;
 
     private ColorPickerPreference mSignal;
     private ColorPickerPreference mNoSim;
     private ColorPickerPreference mAirplaneMode;
     private ColorPickerPreference mColor;
     private ColorPickerPreference mStatus;
     private ColorPickerPreference mBatteryIcon;
     private ColorPickerPreference mBatteryText;
     private SwitchPreference mColorSwitch;
     private Preference mEnable;
 
     private ContentResolver mResolver;

     @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
	 refreshSettings();
	}

	 public void refreshSettings() {
 	 PreferenceScreen prefs = getPreferenceScreen();
	 if (prefs != null) {
            prefs.removeAll();
        }
 	 addPreferencesFromResource(R.xml.status_bar_colors);
         mResolver = getActivity().getContentResolver();
 
         int intColor;
         String hexColor;
 
         mSignal =
                 (ColorPickerPreference) findPreference(PREF_SIGNAL);
         intColor = Settings.System.getInt(mResolver,
                 Settings.System.STATUS_BAR_NETWORK_ICONS_SIGNAL_COLOR,
                 WHITE); 
         mSignal.setNewPreviewColor(intColor);
         hexColor = String.format("#%08x", (0xffffffff & intColor));
         mSignal.setSummary(hexColor);
         mSignal.setResetColors(WHITE, HOLO_BLUE_LIGHT);
         mSignal.setOnPreferenceChangeListener(this);
 
         mNoSim =
                 (ColorPickerPreference) findPreference(PREF_NO_SIM);
         intColor = Settings.System.getInt(mResolver,
                 Settings.System.STATUS_BAR_NETWORK_ICONS_NO_SIM_COLOR,
                 WHITE); 
         mNoSim.setNewPreviewColor(intColor);
         hexColor = String.format("#%08x", (0xffffffff & intColor));
         mNoSim.setSummary(hexColor);
         mNoSim.setResetColors(WHITE, RED_500);
         mNoSim.setOnPreferenceChangeListener(this);
 
         mAirplaneMode =
                 (ColorPickerPreference) findPreference(PREF_AIRPLANE_MODE);
         intColor = Settings.System.getInt(mResolver,
                 Settings.System.STATUS_BAR_NETWORK_ICONS_AIRPLANE_MODE_COLOR,
                 WHITE); 
         mAirplaneMode.setNewPreviewColor(intColor);
         hexColor = String.format("#%08x", (0xffffffff & intColor));
         mAirplaneMode.setSummary(hexColor);
         mAirplaneMode.setResetColors(WHITE, RED_500);
         mAirplaneMode.setOnPreferenceChangeListener(this);
 
         mBatteryIcon =
                 (ColorPickerPreference) findPreference(BATTERY_ICON);
         intColor = Settings.System.getInt(mResolver,
                 Settings.System.BATTERY_ICON_COLOR,
                 WHITE); 
         mBatteryIcon.setNewPreviewColor(intColor);
         hexColor = String.format("#%08x", (0xffffffff & intColor));
         mBatteryIcon.setSummary(hexColor);
         mBatteryIcon.setResetColors(WHITE, HOLO_BLUE_LIGHT);
         mBatteryIcon.setOnPreferenceChangeListener(this);

         mBatteryText =
                 (ColorPickerPreference) findPreference(BATTERY_TEXT);
         intColor = Settings.System.getInt(mResolver,
                 Settings.System.BATTERY_TEXT_COLOR,
                 WHITE); 
         mBatteryText.setNewPreviewColor(intColor);
         hexColor = String.format("#%08x", (0xffffffff & intColor));
         mBatteryText.setSummary(hexColor);
         mBatteryText.setResetColors(WHITE, HOLO_BLUE_LIGHT);
         mBatteryText.setOnPreferenceChangeListener(this);

         mColor =
                 (ColorPickerPreference) findPreference(PREF_COLOR);
         intColor = Settings.System.getInt(mResolver,
                 Settings.System.STATUS_BAR_NOTIFICATION_ICONS_COLOR,
                 WHITE); 
         mColor.setNewPreviewColor(intColor);
         hexColor = String.format("#%08x", (0xffffffff & intColor));
         mColor.setSummary(hexColor);
         mColor.setResetColors(WHITE, HOLO_BLUE_LIGHT);
         mColor.setOnPreferenceChangeListener(this);
 
         mStatus =
                 (ColorPickerPreference) findPreference(PREF_STATUS);
         intColor = Settings.System.getInt(mResolver,
                 Settings.System.STATUS_BAR_STATUS_ICONS_COLOR,
                 WHITE); 
         mStatus.setNewPreviewColor(intColor);
         hexColor = String.format("#%08x", (0xffffffff & intColor));
         mStatus.setSummary(hexColor);
         mStatus.setResetColors(WHITE, HOLO_BLUE_LIGHT);
         mStatus.setOnPreferenceChangeListener(this);

			
	 mColorSwitch = (SwitchPreference) findPreference(ENABLE_COLORS);
         mColorSwitch.setChecked(Settings.System.getInt(mResolver,
                     Settings.System.STATUSBAR_COLOR_SWITCH, 0) == 1);
         mColorSwitch.setOnPreferenceChangeListener(this);

	mEnable = findPreference(ENABLE);

	} 

	public boolean onOptionsItemSelected(MenuItem item) {
         switch (item.getItemId()) {
             case MENU_RESET:
                 showDialogInner(DLG_RESET);
                 return true;
              default:
                 return super.onContextItemSelected(item);
         }
     }

     public boolean onPreferenceChange(Preference preference, Object newValue) {
         String hex;
         int intHex;
	boolean enable = Settings.System.getInt(getActivity().getContentResolver(),
                    Settings.System.STATUSBAR_COLOR_SWITCH, 0) == 1;
         if (preference == mSignal) {
             hex = ColorPickerPreference.convertToARGB(
                     Integer.valueOf(String.valueOf(newValue)));
             intHex = ColorPickerPreference.convertToColorInt(hex);
             Settings.System.putInt(mResolver,
                     Settings.System.STATUS_BAR_NETWORK_ICONS_SIGNAL_COLOR, intHex);
             preference.setSummary(hex);
             return true;
         } else if (preference == mNoSim) {
             hex = ColorPickerPreference.convertToARGB(
                     Integer.valueOf(String.valueOf(newValue)));
             intHex = ColorPickerPreference.convertToColorInt(hex);
             Settings.System.putInt(mResolver,
                     Settings.System.STATUS_BAR_NETWORK_ICONS_NO_SIM_COLOR, intHex);
             preference.setSummary(hex);
             return true;
         } else if (preference == mAirplaneMode) {
             hex = ColorPickerPreference.convertToARGB(
                     Integer.valueOf(String.valueOf(newValue)));
             intHex = ColorPickerPreference.convertToColorInt(hex);
             Settings.System.putInt(mResolver,
                     Settings.System.STATUS_BAR_NETWORK_ICONS_AIRPLANE_MODE_COLOR,
                     intHex);
             preference.setSummary(hex);
             return true;
          } else if (preference == mColor) {
             hex = ColorPickerPreference.convertToARGB(
                     Integer.valueOf(String.valueOf(newValue)));
             intHex = ColorPickerPreference.convertToColorInt(hex);
             Settings.System.putInt(mResolver,
                     Settings.System.STATUS_BAR_NOTIFICATION_ICONS_COLOR, intHex);
             preference.setSummary(hex);
             return true;
           } else if (preference == mStatus) {
             hex = ColorPickerPreference.convertToARGB(
                     Integer.valueOf(String.valueOf(newValue)));
             intHex = ColorPickerPreference.convertToColorInt(hex);
             Settings.System.putInt(mResolver,
                     Settings.System.STATUS_BAR_STATUS_ICONS_COLOR,
                     intHex);
             preference.setSummary(hex);
             return true;
         }  else if (preference == mBatteryIcon) {
             hex = ColorPickerPreference.convertToARGB(
                     Integer.valueOf(String.valueOf(newValue)));
             intHex = ColorPickerPreference.convertToColorInt(hex);
             Settings.System.putInt(mResolver,
                     Settings.System.BATTERY_ICON_COLOR, intHex);
             preference.setSummary(hex);
             return true;
           }  else if (preference == mBatteryText) {
             hex = ColorPickerPreference.convertToARGB(
                     Integer.valueOf(String.valueOf(newValue)));
             intHex = ColorPickerPreference.convertToColorInt(hex);		
             Settings.System.putInt(mResolver,
                     Settings.System.BATTERY_TEXT_COLOR, intHex);
             preference.setSummary(hex);
             return true;
           } else if (preference == mColorSwitch) {
		boolean value = (Boolean) newValue;
          	Settings.System.putInt(mResolver, Settings.System.STATUSBAR_COLOR_SWITCH, value ? 1 : 0);
             return true;
           }
         return false;
     }
  

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
		if (preference == mEnable) {
		Helpers.restartSystemUI(); 
            }    else {
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        } 
	return false;
	}
    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.APPLICATION;
    }

	private void showDialogInner(int id) {
        DialogFragment newFragment = MyAlertDialogFragment.newInstance(id);
        newFragment.setTargetFragment(this, 0);
        newFragment.show(getFragmentManager(), "dialog " + id);
     }
 
     public static class MyAlertDialogFragment extends DialogFragment {
 
         public static MyAlertDialogFragment newInstance(int id) {
             MyAlertDialogFragment frag = new MyAlertDialogFragment();
             Bundle args = new Bundle();
             args.putInt("id", id);
             frag.setArguments(args);
             return frag;
         }

   	StatusBarColors getOwner() {
            return (StatusBarColors) getTargetFragment();
        }

         @Override
         public Dialog onCreateDialog(Bundle savedInstanceState) {
             int id = getArguments().getInt("id");
             switch (id) {
                 case DLG_RESET:
                     return new AlertDialog.Builder(getActivity())
                     .setTitle(R.string.reset)
                     .setMessage(R.string.dlg_reset_values_message)
                     .setNegativeButton(R.string.cancel, null)
                     .setNeutralButton(R.string.dlg_reset_android,
                         new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int which) {
                             Settings.System.putInt(getOwner().mResolver,
                                     Settings.System.STATUS_BAR_NETWORK_ICONS_SIGNAL_COLOR,
                                     WHITE);
                             Settings.System.putInt(getOwner().mResolver,
                                     Settings.System.STATUS_BAR_NETWORK_ICONS_NO_SIM_COLOR,
                                     WHITE);
                             Settings.System.putInt(getOwner().mResolver,
                                     Settings.System.STATUS_BAR_NETWORK_ICONS_AIRPLANE_MODE_COLOR,
                                     WHITE);
                             Settings.System.putInt(getOwner().mResolver,
                                     Settings.System.STATUS_BAR_NOTIFICATION_ICONS_COLOR,
                                     WHITE);
                             Settings.System.putInt(getOwner().mResolver,
                                     Settings.System.STATUS_BAR_STATUS_ICONS_COLOR,
                                     WHITE);
                             Settings.System.putInt(getOwner().mResolver,
                                     Settings.System.BATTERY_ICON_COLOR,
                                     WHITE);
                             Settings.System.putInt(getOwner().mResolver,
                                     Settings.System.BATTERY_TEXT_COLOR,
                                     WHITE);
                             getOwner().refreshSettings();
                         }
                     })
                     .setPositiveButton(R.string.dlg_reset_rr,
                         new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int which) {
                             Settings.System.putInt(getOwner().mResolver,
                                     Settings.System.STATUS_BAR_NETWORK_ICONS_SIGNAL_COLOR,
                                     HOLO_BLUE_LIGHT);
                             Settings.System.putInt(getOwner().mResolver,
                                     Settings.System.STATUS_BAR_NETWORK_ICONS_NO_SIM_COLOR,
                                     RED_500);
                             Settings.System.putInt(getOwner().mResolver,
                                     Settings.System.STATUS_BAR_NETWORK_ICONS_AIRPLANE_MODE_COLOR,
                                     RED_500);
                             Settings.System.putInt(getOwner().mResolver,
                                     Settings.System.STATUS_BAR_NOTIFICATION_ICONS_COLOR,
                                     HOLO_BLUE_LIGHT);
                             Settings.System.putInt(getOwner().mResolver,
                                     Settings.System.STATUS_BAR_STATUS_ICONS_COLOR,
                                     HOLO_BLUE_LIGHT);
                             Settings.System.putInt(getOwner().mResolver,
                                     Settings.System.BATTERY_ICON_COLOR,
                                     HOLO_BLUE_LIGHT);
                             Settings.System.putInt(getOwner().mResolver,
                                     Settings.System.BATTERY_TEXT_COLOR,
                                     HOLO_BLUE_LIGHT);
                             getOwner().refreshSettings();
                         }
                     })
                     .create();
             }
             throw new IllegalArgumentException("unknown id " +id);
         }
 
         @Override
         public void onCancel(DialogInterface dialog) {
 
         }
     }
     
         	    public static final Indexable.SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                                                                             boolean enabled) {
                     ArrayList<SearchIndexableResource> result =
                             new ArrayList<SearchIndexableResource>();
 
                     SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.status_bar_colors;
                     result.add(sir);
 
                     return result;
                 }
 
                 @Override
                 public List<String> getNonIndexableKeys(Context context) {
                     final List<String> keys = new ArrayList<String>();
                     return keys;
                 }
         };
}
