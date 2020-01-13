package com.faucet.quickutils.core.controler;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.View;

public class BaseFragment extends Fragment {

	protected Context context;

	@Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
                                                                                                                                                                                                                
    @Override
	//下面的代码很关键,没有下面的代码会出现切换tab的时候重影现象
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null)
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
    }

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		this.context = context;
	}

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
