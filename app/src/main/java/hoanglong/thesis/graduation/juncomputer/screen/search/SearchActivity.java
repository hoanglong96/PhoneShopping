package hoanglong.thesis.graduation.juncomputer.screen.search;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hoanglong.thesis.graduation.juncomputer.R;
import hoanglong.thesis.graduation.juncomputer.data.model.phone_product.ItemPhoneProduct;
import hoanglong.thesis.graduation.juncomputer.data.model.phone_product.PhoneProduct;
import hoanglong.thesis.graduation.juncomputer.data.model.search.HistorySearch;
import hoanglong.thesis.graduation.juncomputer.data.model.search.KeySearch;
import hoanglong.thesis.graduation.juncomputer.data.source.local.realm.RealmHistorySearch;
import hoanglong.thesis.graduation.juncomputer.screen.base.BaseActivity;
import hoanglong.thesis.graduation.juncomputer.screen.phone.adapter.PhoneAdapter;
import hoanglong.thesis.graduation.juncomputer.screen.phone.detail_product.DetailProductActivity;
import hoanglong.thesis.graduation.juncomputer.screen.search.adapter.HistorySearchAdapter;
import hoanglong.thesis.graduation.juncomputer.screen.search.adapter.SuggestAdapter;
import hoanglong.thesis.graduation.juncomputer.service.BaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends BaseActivity implements View.OnClickListener, TextWatcher,
        SuggestAdapter.OnItemClickListener, HistorySearchAdapter.OnItemClickListener
        ,PhoneAdapter.OnClickProductListener,SwipeRefreshLayout.OnRefreshListener {

    private static final int WAITING_TIME = 1000;
    @BindView(R.id.constraint_history)
    ConstraintLayout mConstraintHistory;
    @BindView(R.id.constraint_trending)
    ConstraintLayout mConstraintTrending;
    @BindView(R.id.recycler_trending)
    RecyclerView mRecyclerTrending;
    @BindView(R.id.recycler_search)
    RecyclerView mRecyclerSearch;
    @BindView(R.id.recycler_history)
    RecyclerView mRecyclerHistory;
    @BindView(R.id.ic_delete)
    TextView mTextDelete;
    @BindView(R.id.ic_back)
    ImageView mImageBack;
    @BindView(R.id.edit_search)
    EditText mEditSearch;
    @BindView(R.id.progress_search)
    ProgressBar mProgressSearch;
    @BindView(R.id.swipe_search)
    SwipeRefreshLayout mSwipeSearch;
    private String mSearchKey;
    private List<String> mSuggestSearch;

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_search;
    }

    @Override
    protected void initComponent() {
        ButterKnife.bind(this);
        initListener();
    }

    private void initListener() {
        mImageBack.setOnClickListener(this);
        mTextDelete.setOnClickListener(this);
        mEditSearch.addTextChangedListener(this);
        mSwipeSearch.setOnRefreshListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setDataSuggest();
        mRecyclerTrending.setAdapter(new SuggestAdapter(mSuggestSearch, this));
        if (RealmHistorySearch.getListHistorySearch() != null) {
            mRecyclerHistory.setAdapter(
                    new HistorySearchAdapter(
                            RealmHistorySearch.getListHistorySearch(), this));
        }
    }

    private void setDataSuggest() {
        mSuggestSearch = new ArrayList<>();
        mSuggestSearch.add("Iphone Xs max");
        mSuggestSearch.add("Samsung galaxy s9");
        mSuggestSearch.add("Oppo find X");
        mSuggestSearch.add("Iphone Xr");
        mSuggestSearch.add("Nokia");
        mSuggestSearch.add("BPhone 3");
        mSuggestSearch.add("Samsung Note 9");
        mSuggestSearch.add("Huawei mate 20 pro");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_back:
                onBackPressed();
                break;
            case R.id.ic_delete:
                break;
        }
    }

    private CountDownTimer mCountDownTimer = new CountDownTimer(WAITING_TIME, WAITING_TIME) {

        public void onTick(long millisUntilFinished) {
        }

        public void onFinish() {
            loadDataSearch();
        }
    };

    private void loadDataSearch() {
        visibilityView(false);
        HistorySearch historySearch = new HistorySearch(mSearchKey);
        RealmHistorySearch.addHistorySearch(historySearch);

        KeySearch keySearch = new KeySearch(mSearchKey);
        Call<PhoneProduct> call = BaseService.getService().getSearch(keySearch);
        call.enqueue(new Callback<PhoneProduct>() {
            @Override
            public void onResponse(@NonNull Call<PhoneProduct> call, @NonNull Response<PhoneProduct> response) {
                if (response.body() != null) {
                    mProgressSearch.setVisibility(View.GONE);
                    PhoneAdapter phoneAdapter = new PhoneAdapter(
                            response.body().getPhoneProduct(),
                            SearchActivity.this);
                    mRecyclerSearch.setAdapter(phoneAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<PhoneProduct> call, @NonNull Throwable t) {
                mProgressSearch.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mSearchKey = mEditSearch.getText().toString().trim();
        if (!mSearchKey.isEmpty()) {
            mCountDownTimer.start();
        } else {
            visibilityView(true);
        }
    }

    private void visibilityView(boolean isShow) {
        if (isShow) {
            mConstraintTrending.setVisibility(View.VISIBLE);
            mConstraintHistory.setVisibility(View.VISIBLE);
            mRecyclerSearch.setVisibility(View.GONE);
            mProgressSearch.setVisibility(View.GONE);
        } else {
            mConstraintTrending.setVisibility(View.GONE);
            mConstraintHistory.setVisibility(View.GONE);
            mRecyclerSearch.setVisibility(View.VISIBLE);
            mProgressSearch.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClickSuggestListener(String keySuggest) {
        mSearchKey = keySuggest;
        mEditSearch.setText(keySuggest);
    }

    @Override
    public void onItemClickHistorySearchListener(HistorySearch historySearch) {
        mEditSearch.setText(historySearch.getHistorySearch());
    }

    @Override
    public void onClickItemProduct(ItemPhoneProduct itemPhoneProduct) {
        Intent intent = new Intent(SearchActivity.this,DetailProductActivity.class);
        intent.putExtra("BUNDLE_ITEM_PRODUCT", itemPhoneProduct.getTitle());
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeSearch.setRefreshing(false);
                loadDataSearch();
            }
        }, 1000);
    }
}
