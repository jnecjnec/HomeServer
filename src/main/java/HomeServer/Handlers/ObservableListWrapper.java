/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HomeServer.Handlers;

import HomeServer.Handlers.Device.Device;
import HomeServer.Handlers.User.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author jnec
 */
public class ObservableListWrapper {

    private final ObservableList<User> users = FXCollections.observableArrayList();
    private final ObservableList<Device> devices = FXCollections.observableArrayList();
  
    public ObservableList<User> getUsers() {
        return users;
    }

    public User findUserByName(String name) {
        User u = null;
        for (User i : users) {
            if (i.getName().getValue().equals(name)) {
                u = i;
                break;
            }
        }
        return u;
    }

    public boolean addUser(String name, String password) {
        if (findUserByName(name) == null) {
            User u = new User();
            u.setName(name);
            u.setPasswordHash(password);
            return users.add(u);
        } else {
            return false;
        }
    }

    public boolean delUser(String name) {
        User u = findUserByName(name);
        if (u != null) {
            return users.remove(u);
        } else {
            return false;
        }
    }
    
     public ObservableList<Device> getDevices() {
        return devices;
    }
}
