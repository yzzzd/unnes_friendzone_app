package com.friend.zone.ui.login

import android.content.Intent
import android.os.Bundle
import com.friend.zone.R
import com.friend.zone.databinding.ActivityLoginBinding
import com.friend.zone.ui.home.HomeActivity
import com.friend.zone.ui.register.RegisterActivity
import com.nuryazid.core.api.ApiStatus
import com.nuryazid.core.base.activity.CoreActivity

class LoginActivity : CoreActivity<ActivityLoginBinding, LoginViewModel>(R.layout.activity_login) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.activity = this

        viewModel.apiResponse.observe(this, { response ->
            when (response.status) {
                ApiStatus.LOADING -> loadingDialog.show(response.message)
                ApiStatus.ERROR -> loadingDialog.show(response.message, false)
                ApiStatus.SUCCESS -> {
                    loadingDialog.dismiss()
                    val homeIntent = Intent(this, HomeActivity::class.java)
                    startActivity(homeIntent)
                    finish()
                }
            }
        })
    }

    fun openRegister() {
        val registerIntent = Intent(this, RegisterActivity::class.java)
        startActivity(registerIntent)
    }
}