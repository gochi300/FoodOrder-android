package com.mubita.foodorderapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.mubita.foodorderapp.models.Notification;
import com.mubita.foodorderapp.repository.NotificationRepository;

import java.util.List;

public class NotificationViewModel extends AndroidViewModel {
    private NotificationRepository repository;
    private LiveData<List<Notification>> allNotifications;

    public NotificationViewModel(@NonNull Application application) {
        super(application);
        repository = new NotificationRepository(application);
        allNotifications = repository.getAllNotifications();
    }

    public void insert(Notification notification) {
        repository.insert(notification);
    }

    public void update(Notification notification) {
        repository.update(notification);
    }

    public void delete(Notification notification) {
        repository.delete(notification);
    }

    public void deleteAllNotifications() {
        repository.deleteAllNotifications();
    }

    public LiveData<List<Notification>> getAllNotifications() {
        return allNotifications;
    }
}
