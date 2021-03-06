package com.stockingd.cinedex;

import android.content.Context;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class CinedexModule {

    @Provides
    @Singleton
    Picasso providesPicasso(Context context) {
        return Picasso.with(context);
    }

    @Provides
    @Singleton
    EventBus providesEventBus() {
        return EventBus.builder().addIndex(new EventBusIndex()).build();
    }
}
