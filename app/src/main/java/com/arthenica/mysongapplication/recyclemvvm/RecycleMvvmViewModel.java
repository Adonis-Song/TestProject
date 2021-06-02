package com.arthenica.mysongapplication.recyclemvvm;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.arthenica.mysongapplication.BR;

import java.util.ArrayList;
import java.util.List;

public class RecycleMvvmViewModel extends ViewModel {
	// TODO: Implement the ViewModel

	private LiveData<Long> mTest = new MutableLiveData<>();
	private ObservableList<ObservableMvvmBean> mCaptionList = new ObservableArrayList<>();

	public LiveData<Long> getTest() {
		return mTest;
	}

	public void initCaptionList(List<RecycleMvvmBean> captionBeanList) {
		List<ObservableMvvmBean> list = new ArrayList<>();
		for (RecycleMvvmBean bean : captionBeanList) {
			list.add(new ObservableMvvmBean(bean));
		}
		mCaptionList.addAll(list);
	}

	public static class ObservableMvvmBean extends BaseObservable {
		private RecycleMvvmBean bean;

		public ObservableMvvmBean(RecycleMvvmBean bean) {
			this.bean = bean;
		}

		@Bindable
		public String getText() {
			return bean.getText();
		}

		public void setText(String text) {
			if (text == null) {
				bean.setText("");
				notifyPropertyChanged(BR.bean);
			} else if (!text.equals(bean.getText())) {
				bean.setText(text);
				notifyPropertyChanged(BR.bean);
			}
		}
	}
}