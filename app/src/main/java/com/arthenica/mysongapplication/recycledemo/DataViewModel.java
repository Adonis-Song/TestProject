package com.arthenica.mysongapplication.recycledemo;

import android.provider.ContactsContract;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class DataViewModel extends ViewModel {
	private MutableLiveData<ArrayList<Data>> datas;

	public MutableLiveData<ArrayList<Data>> getData() {
		if (datas == null) {
			datas = new MutableLiveData<>();
			datas.setValue(new ArrayList<>());
		}
		return datas;
	}

	public void addData(Data data) {
		if (datas == null) {
			datas = new MutableLiveData<>();
			datas.setValue(new ArrayList<>());
		}
		datas.getValue().add(data);
	}

	public void addData(List<Data> dataList) {
		if (datas == null) {
			datas = new MutableLiveData<>();
			datas.setValue(new ArrayList<>());
		}
		datas.getValue().addAll(dataList);
	}

	public static class Data {
		private String name;
		public Data(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	@Override
	protected void onCleared() {
		datas = null;
		super.onCleared();
	}
}
