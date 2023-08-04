package com.ss.user.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.event.MenuActionEvent;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import io.jsonwebtoken.Claims;
import com.ss.message.Constant;

@ManagedBean
@SessionScoped
public class MenuBean implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MenuModel menuModel;
	private static final Logger logger = LogManager.getLogger(MenuBean.class);
    public MenuBean() {
        menuModel = new DefaultMenuModel();
        createMenuItems();
    }

    private void createMenuItems() {
        // Get the user's role (you need to implement your own logic to get the role)
        Long userRole=getUserRoleIdFromRequest();
        // Define the menu items and their structure based on the user's role
        Map<String, List<MenuItem>> menuStructure = getMenuStructureForRole(userRole);

     // Create the menu items and structure the menu based on the defined menu items
        for (Map.Entry<String, List<MenuItem>> entry : menuStructure.entrySet()) {
            String menuLabel = entry.getKey();
            List<MenuItem> submenuItems = entry.getValue();

            if (submenuItems.size() == 1) {
                // If there's only one submenu item, add it directly to the menuModel
                MenuItem subItem = submenuItems.get(0);

                DefaultMenuItem subMenuItem = DefaultMenuItem.builder()
                        .value(subItem.getLabel())
                        .outcome(subItem.getUrl())// Use outcome for navigation
                        .build();
//                if (subItem.getUrl().equals(getCurrentPage())) {
//                	subMenuItem.setStyleClass("menuSelected");
//                }
                // Set the action for the menu item
            //  subMenuItem.setCommand("#{menuBean.doSomething}");
            //  subMenuItem.seton("selectMenu('"+subItem.getLabel()+"')");
           //   subMenuItem.setOncomplete("selectMenu('"+subItem.getLabel()+"')");
              menuModel.getElements().add(subMenuItem);
            } else if (submenuItems.size() > 1) {
                // If there are multiple submenu items, create a submenu and add submenu items to it
                DefaultSubMenu menuItem = DefaultSubMenu.builder().label(menuLabel).build();

                for (MenuItem subItem : submenuItems) {
                    DefaultMenuItem subMenuItem = DefaultMenuItem.builder()
                            .value(subItem.getLabel())
                            .outcome(subItem.getUrl()) // Use outcome for navigation
                            .build();

                    menuItem.getElements().add(subMenuItem);
                }

                menuModel.getElements().add(menuItem);
            }
        }
    }

    private Map<String, List<MenuItem>> getMenuStructureForRole(Long userRole) {
        // You can implement your logic here to return a map of menu labels and their corresponding submenu items
        // For example, you can query a database or use conditional statements to determine the menu structure based on the user's role.
        // Here's a simple example:
    	//This can be picked from DB or any other Shared storage without restart also menu can change
    	//Using arraylist is not good option here.
        Map<String, List<MenuItem>> menuStructure = new HashMap<>();
        logger.debug("getMenuStructureForRole "+userRole);
        if (userRole==1) {
            List<MenuItem> adminMenuItems = new ArrayList<>();
            adminMenuItems.add(new MenuItem("Dashboard", "/dashboard"));
            
            List<MenuItem> usersMenuItems = new ArrayList<>();
            usersMenuItems.add(new MenuItem("Active", "/card"));
            usersMenuItems.add(new MenuItem("Deactive", "/card"));
            
            menuStructure.put("Dashboard", adminMenuItems);
            menuStructure.put("Users", usersMenuItems);
        } else if (userRole==2) {
            List<MenuItem> userMenuItems = new ArrayList<>();
            userMenuItems.add(new MenuItem("Dashboard", "/dashboard"));
            List<MenuItem> profileItems = new ArrayList<>();
            profileItems.add(new MenuItem("Profile", "/profile"));
            menuStructure.put("Dashboard", userMenuItems);
            menuStructure.put("Profile", profileItems);
           
        }

        // You can add more menu structures for other roles if needed

        return menuStructure;
    }
 // Method to get the user's role ID from the request attributes
    private Long getUserRoleIdFromRequest() {
    
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Claims claims = (Claims) externalContext.getRequestMap().get(Constant.USER_PALYLOAD);
        if (claims != null) {
        	logger.debug("claims "+ claims.toString());
            return claims.get("roleId", Long.class);
        }else
        	return null;
    }
  public void doSomething(MenuActionEvent event) {
	  logger.info("doSomething: "+ event);
	  UIComponent component=event.getComponent();
	//  UIComponent parent=event.getComponent().getParent();
	  logger.info("doSomething:: ID: "+component.getId()+" "+component);
	  logger.info("doSomething: childer"+ component.getChildren().size());                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
	// Get the ExternalContext
      ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
      //event.getMenuItem().setStyleClass("menuSelected");
      try {
          // Perform programmatic navigation to the desired URL
          externalContext.redirect("/jsf-maven-project/card");
      } catch (IOException e) {
          // Handle the exception, if needed
      }
  }

    // Inner class to represent menu items
    private static class MenuItem {
        private String label;
        private String url;

        public MenuItem(String label, String url) {
            this.label = label;
            this.url = url;
        }

        public String getLabel() {
            return label;
        }

        public String getUrl() {
            return url;
        }
    }

    // Getter for the menu model
    public MenuModel getMenuModel() {
        return menuModel;
    }
}
