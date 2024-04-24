package com.purple.iptv.player.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xunison.recordingplugin.R;


public class PageHeaderView extends RelativeLayout implements View.OnClickListener {

    private static final String TAG = "PageHeaderView";
    Context mContext;
    public ImageView header_btn_back;
    public View header_helper_view;
    public TextView header_title;
    public TextView header_pre_title;
    public TextView header_time;
    public TextView header_date;
    public ImageView header_menu;
    public ImageView header_search;
    public RelativeLayout header_rl_normal_header;
    public RelativeLayout header_rl_search_header;
    public ImageView header_iv_search_back;
//    public SearchEditTextView et_search;
    public ImageView header_btn_search_cancel;
    public LinearLayout header_ll_add_view;
    public TextView header_ll_add_view_text;
    private PopupWindow popupWindow;
    public FrameLayout frame_cast;
    public ImageView btn_cast_on;
    public ImageView btn_cast_off;

    public PageHeaderView(Context context) {
        super(context);
        mContext = context;
    }

    public PageHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public PageHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater li = LayoutInflater.from(getContext());
        View headerView = li.inflate(R.layout.layout_header_view, this, false);
        header_rl_normal_header = (RelativeLayout) headerView.findViewById(R.id.rl_normal_header);
        header_btn_back = (ImageView) headerView.findViewById(R.id.header_back_icon);
        header_helper_view = (View) headerView.findViewById(R.id.helper_view);
        header_title = (TextView) headerView.findViewById(R.id.header_title);
        header_pre_title = (TextView) headerView.findViewById(R.id.header_pre_title);
        header_time = (TextView) headerView.findViewById(R.id.header_time);
        header_date = (TextView) headerView.findViewById(R.id.header_date);
        header_search = (ImageView) headerView.findViewById(R.id.header_search);
        header_menu = (ImageView) headerView.findViewById(R.id.header_menu);
        header_rl_search_header = (RelativeLayout) headerView.findViewById(R.id.rl_search_header);
        header_iv_search_back = (ImageView) headerView.findViewById(R.id.iv_search_back);
     //   et_search = (SearchEditTextView) headerView.findViewById(R.id.et_search);
        header_btn_search_cancel = (ImageView) headerView.findViewById(R.id.btn_search_cancel);
        header_ll_add_view = (LinearLayout) headerView.findViewById(R.id.ll_add_view);
        header_ll_add_view_text = (TextView) headerView.findViewById(R.id.ll_add_view_text);
        frame_cast = (FrameLayout) headerView.findViewById(R.id.frame_cast);
        btn_cast_on = (ImageView) headerView.findViewById(R.id.btn_cast_on);
        btn_cast_off = (ImageView) headerView.findViewById(R.id.btn_cast_off);

        header_btn_back.setOnClickListener(this);
        header_search.setOnClickListener(this);
        header_menu.setOnClickListener(this);
        header_iv_search_back.setOnClickListener(this);
        header_btn_search_cancel.setOnClickListener(this);
        btn_cast_on.setOnClickListener(this);
        btn_cast_off.setOnClickListener(this);

        //toggleCastBtn();
//        UtilConstant.currently_selected_sort = Config.SORT_BY_DEFAULT;
        addView(headerView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_back_icon:
                ((Activity) mContext).finish();
                break;
//            case R.id.header_search:
//               if (BuildConfig.DEBUG) Log.e(TAG, "onClick: header_search");
//                header_search.clearFocus();
//                header_rl_search_header.setVisibility(VISIBLE);
//                header_rl_normal_header.setVisibility(GONE);
//                et_search.requestFocus();
//                break;
            case R.id.header_menu:
                //openPopup(view, null);
                break;
//            case R.id.iv_search_back:
//                header_rl_search_header.setVisibility(GONE);
//                header_rl_normal_header.setVisibility(VISIBLE);
//                et_search.setText("");
//
//                break;
//            case R.id.btn_search_cancel:
//                et_search.setText("");
//                break;
            case R.id.btn_cast_on:
              //  openCastDialog();
                break;
//            case R.id.btn_cast_off:
//                RemoteConfigModel remoteConfigModel = MyApplication.getInstance().
//                        getPrefManager().getRemoteConfig();
//                if (remoteConfigModel != null && !remoteConfigModel.isIs_subscribed() &&
//                        ((BaseActivity) mContext).rewardedVideoAd != null &&
//                        remoteConfigModel.getSub_in_app_status() &&
//                        CommonMethods.hasRewardedAd(((BaseActivity) mContext).rewardedVideoAd)) {
//                    CommonMethods.showRewardedAdDialog(mContext,
//                            mContext.getString(R.string.str_rewarded_unlock_cast_header),
//                            mContext.getString(R.string.str_rewarded_unlock_cast_text),
//                            ((BaseActivity) mContext).rewardedVideoAd);
//                } else {
//                    openCastDialog();
//                }
//                break;
        }
    }

//    public void openPopup(View v, final CustomInterface.SortByListener sortListener) {
//        if (popupWindow != null) {
//            popupWindow.dismiss();
//        }
//        final LayoutInflater inflater = (LayoutInflater)
//                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        assert inflater != null;
//        View view = inflater.inflate(R.layout.layout_popup_header, null);
//        LinearLayout linear_home = (LinearLayout) view.findViewById(R.id.linear_home);
//        LinearLayout linear_sorting = (LinearLayout) view.findViewById(R.id.linear_sorting);
//        LinearLayout linear_refresh_data = (LinearLayout) view.findViewById(R.id.linear_refresh_data);
//        LinearLayout linear_refresh_epg_data = (LinearLayout) view.findViewById(R.id.linear_refresh_epg_data);
//        LinearLayout linear_settings = (LinearLayout) view.findViewById(R.id.linear_settings);
//        LinearLayout linear_logout = (LinearLayout) view.findViewById(R.id.linear_logout);
//        LinearLayout linear_close = (LinearLayout) view.findViewById(R.id.linear_close);
//        LinearLayout linear_parentalsetting = (LinearLayout) view.findViewById(R.id.linear_parentalsetting);
//        LinearLayout linear_showhidearchive = (LinearLayout) view.findViewById(R.id.linear_showhidearchive);
//
//        popupWindow = new PopupWindow(view, (int) mContext.getResources().
//                getDimension(R.dimen.popup_dialog_width),
//                FrameLayout.LayoutParams.WRAP_CONTENT, true);
//        ConnectionInfoModel connectionInfoModel = null;
//        if (mContext instanceof CategoryListActivity) {
//            linear_parentalsetting.setVisibility(View.VISIBLE);
//            linear_showhidearchive.setVisibility(View.VISIBLE);
//            linear_sorting.setVisibility(View.VISIBLE);
//            connectionInfoModel = ((CategoryListActivity) mContext).connectionInfoModel;
//        } else if (mContext instanceof MovieSeriesActivity) {
//            linear_sorting.setVisibility(View.VISIBLE);
//            connectionInfoModel = ((MovieSeriesActivity) mContext).connectionInfoModel;
//        } else if (mContext instanceof MovieSeriesDetailActivity) {
//            linear_sorting.setVisibility(View.GONE);
//            connectionInfoModel = ((MovieSeriesDetailActivity) mContext).connectionInfoModel;
//        } else if (mContext instanceof SettingListActivity) {
//            linear_sorting.setVisibility(View.GONE);
//            connectionInfoModel = ((SettingListActivity) mContext).connectionInfoModel;
//        } else if (mContext instanceof SettingsFragmentActivity) {
//            linear_sorting.setVisibility(View.GONE);
//            connectionInfoModel = ((SettingsFragmentActivity) mContext).connectionInfoModel;
//        } else if (mContext instanceof UniversalSearchHistoryLiveActivity) {
//            linear_sorting.setVisibility(View.GONE);
//            connectionInfoModel = ((UniversalSearchHistoryLiveActivity) mContext).connectionInfoModel;
//        }
//
//
//        final ConnectionInfoModel finalConnectionInfoModel = connectionInfoModel;
//        linear_home.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (popupWindow != null)
//                    popupWindow.dismiss();
//                if (finalConnectionInfoModel != null) {
//                    Intent intent = new Intent(mContext, DashBoardActivity.class);
//                    intent.putExtra("connectionInfoModel", finalConnectionInfoModel);
//                    mContext.startActivity(intent);
//                }
//            }
//        });
//
//        linear_parentalsetting.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (popupWindow != null) {
//                    popupWindow.dismiss();
//                }
//                Intent i = new Intent(mContext, SettingsFragmentActivity.class);
//                i.putExtra("req_tag", Config.SETTINGS_PARENTAL_CONTROL);
//                i.putExtra("connectionInfoModel", finalConnectionInfoModel);
//                mContext.startActivity(i);
//
//
//            }
//        });
//
//        linear_showhidearchive.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (popupWindow != null) {
//                    popupWindow.dismiss();
//                }
//                if (MyApplication.getInstance().getPrefManager().getSHOWHIDE_ARCHIVE_LIVETV()) {
//                    MyApplication.getInstance().getPrefManager().setSHOWHIDE_ARCHIVE_LIVETV(false);
//                } else {
//                    MyApplication.getInstance().getPrefManager().setSHOWHIDE_ARCHIVE_LIVETV(true);
//                }
//
//
//            }
//        });
//        linear_sorting.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (popupWindow != null)
//                    popupWindow.dismiss();
//                if (sortListener != null) {
//                    CustomDialogs.showSortingDialog(mContext, sortListener);
//                }
//
//            }
//        });
//
//        linear_settings.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (popupWindow != null)
//                    popupWindow.dismiss();
//                Intent i = new Intent(mContext, SettingListActivity.class);
//                i.putExtra("connectionInfoModel", finalConnectionInfoModel);
//                mContext.startActivity(i);
//            }
//        });
//
//        linear_close.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (popupWindow != null)
//                    popupWindow.dismiss();
//            }
//        });
//
//
//        if (popupWindow != null && v != null) {
//            popupWindow.showAsDropDown(v, 0, 0);
//        }
//
//    }


//    public void openPopupwithArchive(final String datatype, View v, final CustomInterface.SortByListener sortListener, final CustomInterface.ArchiveListener archiveListener) {
//        if (popupWindow != null) {
//            popupWindow.dismiss();
//        }
//        final LayoutInflater inflater = (LayoutInflater)
//                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        assert inflater != null;
//        View view = inflater.inflate(R.layout.layout_popup_header, null);
//        LinearLayout linear_home = (LinearLayout) view.findViewById(R.id.linear_home);
//        LinearLayout linear_sorting = (LinearLayout) view.findViewById(R.id.linear_sorting);
//        LinearLayout linear_refresh_data = (LinearLayout) view.findViewById(R.id.linear_refresh_data);
//        LinearLayout linear_refresh_epg_data = (LinearLayout) view.findViewById(R.id.linear_refresh_epg_data);
//        LinearLayout linear_settings = (LinearLayout) view.findViewById(R.id.linear_settings);
//        LinearLayout linear_logout = (LinearLayout) view.findViewById(R.id.linear_logout);
//        LinearLayout linear_close = (LinearLayout) view.findViewById(R.id.linear_close);
//        LinearLayout linear_parentalsetting = (LinearLayout) view.findViewById(R.id.linear_parentalsetting);
//        LinearLayout linear_showhidearchive = (LinearLayout) view.findViewById(R.id.linear_showhidearchive);
//
//        popupWindow = new PopupWindow(view, (int) mContext.getResources().
//                getDimension(R.dimen.popup_dialog_width),
//                FrameLayout.LayoutParams.WRAP_CONTENT, true);
//        ConnectionInfoModel connectionInfoModel = null;
//        if (mContext instanceof CategoryListActivity) {
//            linear_parentalsetting.setVisibility(View.VISIBLE);
//            linear_showhidearchive.setVisibility(View.VISIBLE);
//            linear_sorting.setVisibility(View.VISIBLE);
//            connectionInfoModel = ((CategoryListActivity) mContext).connectionInfoModel;
//        } else if (mContext instanceof MovieSeriesActivity) {
//            linear_sorting.setVisibility(View.VISIBLE);
//            connectionInfoModel = ((MovieSeriesActivity) mContext).connectionInfoModel;
//        } else if (mContext instanceof MovieSeriesDetailActivity) {
//            linear_sorting.setVisibility(View.GONE);
//            connectionInfoModel = ((MovieSeriesDetailActivity) mContext).connectionInfoModel;
//        } else if (mContext instanceof SettingListActivity) {
//            linear_sorting.setVisibility(View.GONE);
//            connectionInfoModel = ((SettingListActivity) mContext).connectionInfoModel;
//        } else if (mContext instanceof SettingsFragmentActivity) {
//            linear_sorting.setVisibility(View.GONE);
//            connectionInfoModel = ((SettingsFragmentActivity) mContext).connectionInfoModel;
//        } else if (mContext instanceof UniversalSearchHistoryLiveActivity) {
//            linear_sorting.setVisibility(View.GONE);
//            connectionInfoModel = ((UniversalSearchHistoryLiveActivity) mContext).connectionInfoModel;
//        }
//
//
//        final ConnectionInfoModel finalConnectionInfoModel = connectionInfoModel;
//        linear_home.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (popupWindow != null)
//                    popupWindow.dismiss();
//                if (finalConnectionInfoModel != null) {
//                    Intent intent = new Intent(mContext, DashBoardActivity.class);
//                    intent.putExtra("connectionInfoModel", finalConnectionInfoModel);
//                    mContext.startActivity(intent);
//                }
//            }
//        });
//
//        linear_parentalsetting.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (popupWindow != null) {
//                    popupWindow.dismiss();
//                }
//
//
//                Intent i = new Intent(mContext, SettingsFragmentActivity.class);
//                i.putExtra("req_tag", Config.SETTINGS_PARENTAL_CONTROL);
//                i.putExtra("connectionInfoModel", finalConnectionInfoModel);
//                mContext.startActivity(i);
//
//
//            }
//        });
//
//        linear_showhidearchive.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (popupWindow != null) {
//                    popupWindow.dismiss();
//                }
//                switch (datatype) {
//                    case Config.MEDIA_TYPE_MOVIE:
//                        if (MyApplication.getInstance().getPrefManager().getSHOWHIDE_ARCHIVE_MOVIE()) {
//                            MyApplication.getInstance().getPrefManager().setSHOWHIDE_ARCHIVE_MOVIE(false);
//                        } else {
//                            MyApplication.getInstance().getPrefManager().setSHOWHIDE_ARCHIVE_MOVIE(true);
//                        }
//                        break;
//                    case Config.MEDIA_TYPE_LIVE:
//                        if (MyApplication.getInstance().getPrefManager().getSHOWHIDE_ARCHIVE_LIVETV()) {
//                            MyApplication.getInstance().getPrefManager().setSHOWHIDE_ARCHIVE_LIVETV(false);
//                        } else {
//                            MyApplication.getInstance().getPrefManager().setSHOWHIDE_ARCHIVE_LIVETV(true);
//                        }
//                        break;
//                    case Config.MEDIA_TYPE_SERIES:
//                        if (MyApplication.getInstance().getPrefManager().getSHOWHIDE_ARCHIVE_SERIES()) {
//                            MyApplication.getInstance().getPrefManager().setSHOWHIDE_ARCHIVE_SERIES(false);
//                        } else {
//                            MyApplication.getInstance().getPrefManager().setSHOWHIDE_ARCHIVE_SERIES(true);
//                        }
//                        break;
//
//                    case MEDIA_TYPE_EPG:
//                        if (MyApplication.getInstance().getPrefManager().getSHOWHIDE_ARCHIVE_EPG()) {
//                            MyApplication.getInstance().getPrefManager().setSHOWHIDE_ARCHIVE_EPG(false);
//                        } else {
//                            MyApplication.getInstance().getPrefManager().setSHOWHIDE_ARCHIVE_EPG(true);
//                        }
//                        break;
//                    case MEDIA_TYPE_CATCH_UP:
//                        if (MyApplication.getInstance().getPrefManager().getSHOWHIDE_ARCHIVE_CATCHUP()) {
//                            MyApplication.getInstance().getPrefManager().setSHOWHIDE_ARCHIVE_CATCHUP(false);
//                        } else {
//                            MyApplication.getInstance().getPrefManager().setSHOWHIDE_ARCHIVE_CATCHUP(true);
//                        }
//                        break;
//                }
//
//                if (archiveListener != null) {
//                    archiveListener.onarchive();
//                }
//
//
//            }
//        });
//        linear_sorting.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (popupWindow != null)
//                    popupWindow.dismiss();
//                if (sortListener != null) {
//                    CustomDialogs.showSortingDialog(mContext, sortListener);
//                }
//
//            }
//        });
//
//        linear_settings.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (popupWindow != null)
//                    popupWindow.dismiss();
//                Intent i = new Intent(mContext, SettingListActivity.class);
//                i.putExtra("connectionInfoModel", finalConnectionInfoModel);
//                mContext.startActivity(i);
//            }
//        });
//
//        linear_close.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (popupWindow != null)
//                    popupWindow.dismiss();
//            }
//        });
//
//
//        if (popupWindow != null && v != null) {
//            popupWindow.showAsDropDown(v, 0, 0);
//        }
//
//    }

//    private void openCastDialog() {
//        CustomDialogs.showCastDialog(mContext, new CustomInterface.dialogCastDevicesListener() {
//            @Override
//            public void onDeviceReady(Dialog dialog, ConnectableDevice device) {
//                btn_cast_on.setVisibility(View.VISIBLE);
//                btn_cast_off.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onCancel(Dialog dialog) {
//
//            }
//
//            @Override
//            public void onStopCasting(Dialog dialog) {
//                btn_cast_on.setVisibility(View.GONE);
//                btn_cast_off.setVisibility(View.VISIBLE);
//            }
//        });
//
//    }

//    public void toggleCastBtn() {
//        boolean is_TV = false;
//        UiModeManager uiModeManager = (UiModeManager) mContext.getSystemService(UI_MODE_SERVICE);
//        if (uiModeManager != null &&
//                uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION) {
//            UtilMethods.LogMethod("yiMode123_", String.valueOf(uiModeManager.getCurrentModeType()));
//            is_TV = true;
//        }
//        if (!is_TV) {
//            if (UtilConstant.connected_device != null) {
//                btn_cast_on.setVisibility(View.VISIBLE);
//                btn_cast_off.setVisibility(View.GONE);
//            } else {
//                btn_cast_off.setVisibility(View.VISIBLE);
//                btn_cast_on.setVisibility(View.GONE);
//            }
//        } else {
//            frame_cast.setVisibility(View.GONE);
//        }
//    }


}
