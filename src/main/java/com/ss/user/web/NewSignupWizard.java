package com.ss.user.web;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;



@ManagedBean
@ViewScoped
public class NewSignupWizard implements Serializable {
	private static final long serialVersionUID = 1L;
    private NewSignupBean newSignupBean = new NewSignupBean();
	private static final Logger logger = LogManager.getLogger(NewSignupWizard.class);
    private boolean skip;
    private Map<String, String> data = new HashMap<>();
    @PostConstruct
    public void init() {
    	logger.info("NewSignupWizard init Start");
    	Map<String, String> map = new HashMap<>();
    	map.put("Ahmedabad", "Ahmedabad");
    	map.put("Gurgaon", "Gurgaon");
    	map.put("Mumbai", "Mumbai");
        newSignupBean.setCities(map); 
        
        data.put("Ahmedabad", "380001");
        data.put("Gurgaon", "122001");
        data.put("Mumbai", "400001");
        newSignupBean.setMaxDate(LocalDate.now());
        logger.info("NewSignupWizard init End");
    }
    public NewSignupBean getNewSignupBean() {
        return newSignupBean;
    }

    public void setNewSignupBean(NewSignupBean newSignupBean) {
        this.newSignupBean = newSignupBean;
    }
    
    public void onCityChange() {
    	logger.info("NewSignupWizard onCityChange Start");
        if (newSignupBean.getCity() != null && !"".equals(newSignupBean.getCity() )) {
        	newSignupBean.setPostalCode(data.get(newSignupBean.getCity()));
        }
        else {
        	newSignupBean.setPostalCode("000000");
        }
        logger.info("NewSignupWizard onCityChange End");
    }
    public void onDateSelect(SelectEvent<LocalDate> event) {
    	logger.info("NewSignupWizard onDateSelect Start");
    	LocalDate selectedDate=event.getObject();
    	newSignupBean.setAge(Period.between(selectedDate, LocalDate.now()).getYears());  
    	LocalDate newDate=selectedDate.plusYears(18);
    	FacesContext facesContext = FacesContext.getCurrentInstance();
    	if(newDate.compareTo(LocalDate.now())>0) {
    	      facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Date Selected is less then 18 years",""));
    	}
    	else
    	{
    		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    	     facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected",selectedDate.format(formatter)));
    	}
    	 logger.info("NewSignupWizard onDateSelect End");
    }
    public void save() {
    	logger.info("NewSignupWizard save Start");
        FacesMessage msg = new FacesMessage("Successful ", "Welcome :" + newSignupBean.getFirstname()+" email" + newSignupBean.getEmail());
        logger.debug("Successful "+ "Welcome :" + newSignupBean.getFirstname()+" email" + newSignupBean.getEmail());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        logger.info("NewSignupWizard save Start");
    }
    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public String onFlowProcess(FlowEvent event) {
        if (skip) {
        	skip = false; //reset in case user goes back
            return "confirm";
        }
        else {
            return event.getNewStep();
        }
    }
}