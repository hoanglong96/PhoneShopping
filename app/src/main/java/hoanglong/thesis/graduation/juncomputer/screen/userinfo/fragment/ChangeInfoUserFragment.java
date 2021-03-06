package hoanglong.thesis.graduation.juncomputer.screen.userinfo.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import hoanglong.thesis.graduation.juncomputer.R;
import hoanglong.thesis.graduation.juncomputer.data.model.user.User;
import hoanglong.thesis.graduation.juncomputer.screen.base.BaseFragment;
import hoanglong.thesis.graduation.juncomputer.service.BaseService;
import hoanglong.thesis.graduation.juncomputer.utils.Constant;
import hoanglong.thesis.graduation.juncomputer.utils.SharedPrefs;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeInfoUserFragment extends BaseFragment implements View.OnClickListener, CheckBox.OnCheckedChangeListener {

    public static final String TAG = ChangeInfoUserFragment.class.getName();
    private User mUser;
    @BindView(R.id.ic_back)
    ImageView mImageBack;
    @BindView(R.id.relative_update_info)
    RelativeLayout mRelativeUpdateInfo;
    @BindView(R.id.et_change_name)
    EditText mEditChangeName;
    @BindView(R.id.et_change_email)
    EditText mEditChangeEmail;
    @BindView(R.id.et_change_birthday)
    EditText mEditChangeBirthday;
    @BindView(R.id.cb_change_password)
    CheckBox mCheckChangePass;
    @BindView(R.id.linear_change_password)
    LinearLayout mLinearChangePass;
    @BindView(R.id.radio_group)
    RadioGroup mRadioSex;
    @BindView(R.id.radioButton_male)
    RadioButton mRadioMale;
    @BindView(R.id.radioButton_female)
    RadioButton mRadioFeMale;
    @BindView(R.id.clear_name)
    ImageView mImageClearName;
    private ProgressDialog mDialogProgress;
    private OnUpdateInfoListener mOnUpdateInfoListener;

    public interface OnUpdateInfoListener{
        void onUpdateInfo();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnUpdateInfoListener = (OnUpdateInfoListener) context;
    }

    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDate();
        }

    };

    private void updateDate() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mEditChangeBirthday.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected int getLayoutResources() {
        return R.layout.fragment_change_info_user;
    }

    @Override
    protected void initComponent(View view) {
        ButterKnife.bind(this, view);
        mLinearChangePass.setVisibility(View.GONE);
        mEditChangeBirthday.setOnClickListener(this);
        mImageBack.setOnClickListener(this);
        mRelativeUpdateInfo.setOnClickListener(this);
        mCheckChangePass.setOnCheckedChangeListener(this);
        mImageClearName.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle saveInstanceState) {
        Gson gson = new Gson();
        String json = SharedPrefs.getInstance().get(Constant.Login.OBJECT_USER_LOGIN, String.class);
        mUser = gson.fromJson(json, User.class);
        if (mUser == null) {
            return;
        }
        mEditChangeEmail.setText(mUser.getEmail());
        mEditChangeEmail.setClickable(false);
        mEditChangeBirthday.setText(mUser.getBirthDay());
        mEditChangeName.setText(mUser.getFullName());
        switch (mUser.getSex()) {
            case "Nam":
                mRadioMale.setChecked(true);
                break;
            case "Nữ":
                mRadioFeMale.setChecked(true);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_back:
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
                break;
            case R.id.relative_update_info:
                checkValid();
                break;
            case R.id.et_change_birthday:
                chooseDate();
                break;
            case R.id.clear_name:
                mEditChangeName.setText("");
                break;
        }
    }

    private void checkValid() {
        if (getContext() == null) {
            return;
        }
        if (mEditChangeName.getText().toString().isEmpty()) {
            Toasty.warning(getContext(), "Bạn không được bỏ trống tên", Toast.LENGTH_SHORT, true).show();
        } else if (mEditChangeBirthday.getText().toString().isEmpty()) {
            Toasty.warning(getContext(), "Bạn không được bỏ trống ngày sinh", Toast.LENGTH_SHORT, true).show();
        } else {
            updateInfo();
        }
    }

    private void chooseDate() {
        if (getContext() == null) {
            return;
        }
        new DatePickerDialog(getContext(), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateInfo() {
        showProgress();
        String name = mEditChangeName.getText().toString();
        String birthday = mEditChangeBirthday.getText().toString();
        String sex = null;
        switch (mRadioSex.getCheckedRadioButtonId()) {
            case R.id.radioButton_male:
                sex = "Nam";
                break;
            case R.id.radioButton_female:
                sex = "Nữ";
                break;
        }
        User user = new User(sex, name, birthday);

        Call<User> call = BaseService.getService().updateInfo(mUser.getEmail(), user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                hideProgress();
                if (response.body() != null) {
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body());
                    SharedPrefs.getInstance().put(Constant.Login.OBJECT_USER_LOGIN, json);

                    if (getFragmentManager() != null) {
                        mOnUpdateInfoListener.onUpdateInfo();
                        getFragmentManager().popBackStack();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                hideProgress();
            }
        });
    }

    public void showProgress() {
        if (mDialogProgress != null) {
            return;
        }
        mDialogProgress = new ProgressDialog(getContext());
        mDialogProgress.setMessage("Cập nhật thông tin");
        mDialogProgress.show();
    }

    public void hideProgress() {
        if (mDialogProgress == null)
            return;
        mDialogProgress.dismiss();
        mDialogProgress = null;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mLinearChangePass.setVisibility(View.VISIBLE);
        } else {
            mLinearChangePass.setVisibility(View.GONE);
        }
    }
}
