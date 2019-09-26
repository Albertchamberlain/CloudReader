package com.example.jingbin.cloudreader.viewmodel.wan;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.jingbin.cloudreader.base.BaseListViewModel;
import com.example.jingbin.cloudreader.bean.CoinBean;
import com.example.jingbin.cloudreader.bean.CoinLogBean;
import com.example.jingbin.cloudreader.bean.wanandroid.BaseResultBean;
import com.example.jingbin.cloudreader.bean.wanandroid.HomeListBean;
import com.example.jingbin.cloudreader.http.HttpClient;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * @author jingbin
 * @data 2019/9/26
 * @Description 积分ViewModel
 */

public class CoinListViewModel extends BaseListViewModel {

    public CoinListViewModel(@NonNull Application application) {
        super(application);
        mPage = 1;
    }

    /**
     * 我的积分
     */
    public MutableLiveData<CoinBean> getCoinLog() {
        final MutableLiveData<CoinBean> data = new MutableLiveData<>();
        Disposable subscribe = HttpClient.Builder.getWanAndroidServer().getCoinLog(mPage)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResultBean<CoinBean>>() {
                    @Override
                    public void accept(BaseResultBean<CoinBean> bean) throws Exception {
                        if (bean != null
                                && bean.getData() != null) {
                            data.setValue(bean.getData());
                        } else {
                            data.setValue(null);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (mPage > 1) {
                            mPage--;
                        }
                        data.setValue(null);
                    }
                });
        addDisposable(subscribe);
        return data;
    }
}