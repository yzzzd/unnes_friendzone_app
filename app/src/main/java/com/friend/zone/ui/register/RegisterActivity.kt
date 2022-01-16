package com.friend.zone.ui.register

import android.os.Bundle
import com.friend.zone.R
import com.friend.zone.databinding.ActivityRegisterBinding
import com.nuryazid.core.api.ApiStatus
import com.nuryazid.core.base.activity.CoreActivity

class RegisterActivity : CoreActivity<ActivityRegisterBinding, RegisterViewModel>(R.layout.activity_register) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.activity = this

        viewModel.apiResponse.observe(this, { response ->
            when (response.status) {
                ApiStatus.LOADING -> loadingDialog.show(response.message)
                ApiStatus.ERROR -> loadingDialog.show(response.message, false)
                ApiStatus.SUCCESS -> loadingDialog.show(response.message, false).onDismiss { finish() }
            }
        })
    }
}