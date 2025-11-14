package com.eco.musicplayer.audioplayer.music.activity.activity.koin

    import com.eco.musicplayer.audioplayer.music.activity.activity.koin.data.DataApiService
    import com.eco.musicplayer.audioplayer.music.activity.activity.koin.repository.UserRepositoryImpl
    import com.eco.musicplayer.audioplayer.music.activity.activity.koin.viewmodel.ProfileViewModel
    import com.eco.musicplayer.audioplayer.music.activity.activity.koin.viewmodel.UserViewModel
    import org.koin.androidx.viewmodel.dsl.viewModel
    import org.koin.dsl.module

val appModule = module {
    single<ApiService> { DataApiService() }
    single<UserRepository> {
        UserRepositoryImpl(
            apiService = get(),
            localCache = mutableMapOf()
        )
    }
    viewModel {UserViewModel(get())  }
    viewModel{ProfileViewModel(get())}

}