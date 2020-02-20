package com.gutotech.narutogame.ui.home.home;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.gutotech.narutogame.R;
import com.gutotech.narutogame.data.model.News;
import com.gutotech.narutogame.data.model.NinjaStatistics;
import com.gutotech.narutogame.data.repository.AuthRepository;
import com.gutotech.narutogame.data.repository.NewsRepository;
import com.gutotech.narutogame.data.repository.NinjaStatisticsRepository;
import com.gutotech.narutogame.ui.ResultListener;

import java.util.List;

public class HomeViewModel extends ViewModel {
    public String email;
    public String password;

    private AuthRepository mAuthRepository;

    private ResultListener mAuthListener;

    public HomeViewModel() {
        mAuthRepository = AuthRepository.getInstance();
    }

    public LiveData<List<News>> getNews() {
        return NewsRepository.getInstance().getAllNews();
    }

    LiveData<List<NinjaStatistics>> getNinjaStatistics() {
        return NinjaStatisticsRepository.getInstance().getAllNinjaStatistics();
    }

    void setAuthListener(ResultListener authListener) {
        mAuthListener = authListener;
    }

    public void onPlayButtonPressed() {
        mAuthListener.onStarted();

        if (validateFields()) {
            mAuthRepository.signIn(email, password,
                    new AuthRepository.Completable() {
                        @Override
                        public void onComplete() {
                            mAuthListener.onSuccess();
                        }

                        @Override
                        public void onError(int resId) {
                            mAuthListener.onFailure(resId);
                        }
                    });
        }
    }

    private boolean validateFields() {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            mAuthListener.onFailure(R.string.email_field_requered);
            valid = false;
        } else if (TextUtils.isEmpty(password)) {
            mAuthListener.onFailure(R.string.password_field_requered);
            valid = false;
        }
        return valid;
    }
}
