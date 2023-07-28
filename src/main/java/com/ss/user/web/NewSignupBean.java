package com.ss.user.web;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.primefaces.model.file.UploadedFile;
import org.primefaces.model.file.UploadedFiles;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import com.ss.user.web.NewSignupBean.InputItem;


public class NewSignupBean implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String firstname;
    private String lastname;
    private LocalDate birthDate;
    private Integer age;
    private String street;
    private String city;
    private Map<String, String> cities;
    private String postalCode;
    private String info;
    private String email;
    private String addEmail;
    private String phone;
    private LocalDate maxDate;
    private UploadedFiles files;
    private UploadedFile file;
    private boolean dynamicAdd;
    private boolean showInput;
    private List<InputItem> inputItems;
    private int inputCounter = 0;
    private MenuModel model;
    
    public NewSignupBean() {
        showInput = false;
        inputItems = new ArrayList<InputItem>();
        model = new DefaultMenuModel();

        //First submenu
        DefaultSubMenu firstSubmenu = DefaultSubMenu.builder()
                .label("Options")
                .expanded(true)
                .build();

        DefaultMenuItem item = DefaultMenuItem.builder()
                .value("Save (Non-Ajax)")
                .icon("pi pi-save")
                .ajax(false)
                .command("#{menuView.save}")
                .update("messages")
                .build();
        firstSubmenu.getElements().add(item);

        item = DefaultMenuItem.builder()
                .value("Update")
                .icon("pi pi-refresh")
                .command("#{menuView.update}")
                .update("messages")
                .build();
        firstSubmenu.getElements().add(item);

        item = DefaultMenuItem.builder()
                .value("Delete")
                .icon("pi pi-times")
                .command("#{menuView.delete}")
                .update("messages")
                .build();
        firstSubmenu.getElements().add(item);

        model.getElements().add(firstSubmenu);

        //Second submenu
        DefaultSubMenu secondSubmenu = DefaultSubMenu.builder()
                .label("Navigations")
                .expanded(false)
                .build();

        item = DefaultMenuItem.builder()
                .value("Website")
                .url("http://www.primefaces.org")
                .icon("pi pi-external-link")
                .build();
        secondSubmenu.getElements().add(item);

        item = DefaultMenuItem.builder()
                .value("Internal")
                .icon("pi pi-upload")
                .command("#{menuView.redirect}")
                .build();
        secondSubmenu.getElements().add(item);

        model.getElements().add(secondSubmenu);
    }
   
    public MenuModel getModel() {
		return model;
	}

	public void setModel(MenuModel model) {
		this.model = model;
	}

	public boolean isShowInput() {
        return showInput;
    }

    public void setShowInput(boolean showInput) {
        this.showInput = showInput;
    }

    public List<InputItem> getInputItems() {
        return inputItems;
    }
    public void addInputItems() {
    	System.out.println("addInputItems");
    	inputItems.add(new InputItem("Input " + (inputCounter++)));
    }
    public void clearInputItems() {
    	 inputItems.clear();
         inputCounter = 0;
    }
	public int getInputCounter() {
		return inputCounter;
	}

	public void setInputCounter(int inputCounter) {
		this.inputCounter = inputCounter;
	}

	public void setInputItems(List<InputItem> inputItems) {
		this.inputItems = inputItems;
	}

	public boolean isDynamicAdd() {
		return dynamicAdd;
	}

	public void setDynamicAdd(boolean dynamicAdd) {
		this.dynamicAdd = dynamicAdd;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public UploadedFiles getFiles() {
		return files;
	}

	public void setFiles(UploadedFiles files) {
		this.files = files;
	}

	public LocalDate getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(LocalDate maxDate) {
		this.maxDate = maxDate;
	}

	public Map<String, String> getCities() {
		return cities;
	}

	public void setCities(Map<String, String> cities) {
		this.cities = cities;
	}

	public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    
    public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getAddEmail() {
		return addEmail;
	}

	public void setAddEmail(String addEmail) {
		this.addEmail = addEmail;
	}

	public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public class InputItem {
        private String label;

        public InputItem(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }
}