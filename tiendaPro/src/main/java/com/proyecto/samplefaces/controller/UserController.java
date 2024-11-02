package com.proyecto.samplefaces.controller;

import com.proyecto.samplefaces.DAO.UserDAO;
import com.proyecto.samplefaces.model.User;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("userController")
@ViewScoped
public class UserController implements Serializable {

    private User user = new User();
    private UserDAO userDAO = new UserDAO();
    private String searchQuery;
    private List<User> searchResults;
    private List<User> favoriteUsers;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @PostConstruct
    public void init() {
        String idParam = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap()
                .get("id");
        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                loadUser(id);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                user = new User();
            }
        }
    }

    public void loadUser(int id) {
        this.user = userDAO.getUserById(id);
    }

    public String addUser() {
        userDAO.addUser(user);
        return "index.xhtml?faces-redirect=true";
    }

    public String updateUser() {
        userDAO.updateUser(user);
        return "index.xhtml?faces-redirect=true";
    }

    public String deleteUser(int id) {
        userDAO.deleteUser(id);
        return "index.xhtml?faces-redirect=true";
    }

    public void searchUsers() {
        if (searchQuery != null && !searchQuery.isEmpty()) {
            searchResults = userDAO.searchUsersByName(searchQuery);
        } else {
            searchResults = userDAO.getAllUsers();
        }
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public List<User> getSearchResults() {
        return searchResults;
    }

    public List<User> getFavoriteUsers() {
        if (favoriteUsers == null) {
            favoriteUsers = userDAO.getFavoriteUsers();
        }
        return favoriteUsers;
    }

    public void toggleFavoriteStatus(User user) {
        boolean newStatus = !user.isFavorite();
        userDAO.updateFavoriteStatus(user.getId(), newStatus);
        user.setFavorite(newStatus);
        favoriteUsers = userDAO.getFavoriteUsers(); // Recargar la lista de favoritos
    }
}
