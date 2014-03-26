package org.phoid.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.component.html.HtmlSelectOneRadio;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * A simple class to abstract the managed bean variables stored by Faces so that
 * if the mechanism for data-storing changes, it's effect on the code won't
 * be that great.
 * 
 * @author Ben Gao
 * 
 */
public final class VariableStore {
    public static enum TypeUIComponent {INPUT_TEXTFIELD, INPUT_SELECTION }; 


	// navigation strings TO DO: move these into a properties file
	public static final String CANCEL_NODE_PROCESSING = "Global:Process-Cancel";
	
	public static Object getItem(String key) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ELContext elContext = facesContext.getELContext();
		Application application = facesContext.getApplication();
		ELResolver variableStore = application.getELResolver();
		return variableStore.getValue(elContext, null, key);

	}

	public static HttpServletRequest getRequest() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		return (HttpServletRequest) facesContext.getExternalContext()
				.getRequest();
	}
	
	public static HttpServletResponse getResponse() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		return (HttpServletResponse) facesContext.getExternalContext()
				.getResponse();
	}	

	public static String getContextPath() {
		return FacesContext.getCurrentInstance().getExternalContext()
				.getRequestContextPath();
	}
	
	public static String getRequestParameter(String paramName) {
		FacesContext context = FacesContext.getCurrentInstance();
	    return (String)context.getExternalContext().getRequestParameterMap().get(paramName);	
	}
	
    private static ClassLoader getCurrentClassLoader(Object defaultObject){
		
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		
		if(loader == null){
			loader = defaultObject.getClass().getClassLoader();
		}
		
		return loader;
	}
    
    public static String getMessageResourceString (
			String bundleName, 
			String key, 
			Object params[], 
			Locale locale) {
    	String text = null;
    	ResourceBundle bundle = 
    		ResourceBundle.getBundle(bundleName, locale, 
						getCurrentClassLoader(params));
    	try {
    		text = bundle.getString(key);
    	} catch(MissingResourceException e){
    		text = "?? key " + key + " not found ??";
    	}

    	if(params != null){
    		MessageFormat mf = new MessageFormat(text, locale);
    		text = mf.format(params, new StringBuffer(), null).toString();
    	}

    	return text;
    }	
    
    /**
     * creates an error message without adding it to the faces context
     */
	public static FacesMessage createErrorMessage (String errorKey, Object[] params) {
		FacesContext context = FacesContext.getCurrentInstance();
	    FacesMessage message = new FacesMessage();
		String messageText = getMessageResourceString(context.getApplication()
				.getMessageBundle(), errorKey, 
				params, context.getViewRoot().getLocale());
    	message.setSummary(messageText);
    	message.setDetail("Fatal");
    	message.setSeverity(FacesMessage.SEVERITY_FATAL);
		return message;
	}
	
	/**
	 * sets an error message based off the key for an error message in a resource bundle
	 */
	public static void generateErrorMessage (String errorKey, Object[] params) {
		FacesContext context = FacesContext.getCurrentInstance();
	    FacesMessage message = new FacesMessage();
		String messageText = getMessageResourceString(context.getApplication()
				.getMessageBundle(), errorKey, 
				params, context.getViewRoot().getLocale());
    	message.setSummary(messageText);
    	message.setDetail("Fatal");
    	message.setSeverity(FacesMessage.SEVERITY_FATAL);
 		context.addMessage(null, message);
	}
	
	/**
	 * sets an error message given the actual message
	 */
	public static FacesMessage generateErrorMessage (String messageText) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		if(!isErrorMessageExists(context,messageText)){		    
	    	message.setSummary(messageText);
	    	message.setDetail("Fatal");
	    	message.setSeverity(FacesMessage.SEVERITY_FATAL);
			context.addMessage(null, message);
		}	
		return message;
	}
	
	private static boolean isErrorMessageExists(FacesContext context,String messageText){		
		FacesMessage message = null;
		for(Iterator<FacesMessage> it=context.getMessages(); it.hasNext();){
			message = it.next() ;
			if(messageText.equals(message.getSummary())){
				return true;
			}
		}		
		return false;		
	}
	
	/**
	 * this method redirects JSF to download a data file, given
	 * it's content type, name, and input stream
	 *
	 */
	public static void downloadDataFile(InputStream fileData, String fileName, String fileContent) 
	    throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();		
		HttpServletResponse response =
	         ( HttpServletResponse ) context.getExternalContext().getResponse();

		response.setContentType(fileContent);
		response.setHeader("Content-Disposition", "attachment;filename=\"" +
				fileName + "\"");
		InputStream is = fileData;
		OutputStream os = response.getOutputStream();

		int read;
		byte[] bytes = new byte[4096];
		while((read = is.read(bytes)) != -1){
			os.write(bytes,0,read);
		}
		os.flush();
		os.close();
		context.responseComplete();
	}
	
	/**
	 * this method redirects JSF to download a data file, given
	 * it's content type, name, and input stream
	 *
	 */
	public static void downloadDataFile(byte[] fileData, String fileName, String fileContent) 
	    throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();		
		HttpServletResponse response =
	         ( HttpServletResponse ) context.getExternalContext().getResponse();

		response.setContentType(fileContent);
		response.setHeader("Content-Disposition", "attachment;filename=\"" +
				fileName + "\"");
		OutputStream os = response.getOutputStream();

		os.write(fileData,0,fileData.length);
		os.flush();
		os.close();
		context.responseComplete();
	}


	
	/**
	 * sets the component value in the UI for a specific form 
	 * good for refreshing the value in-page
	 */
	public static void updateComponent (TypeUIComponent typeComponent, String updateField,
			String updateValue) {
		FacesContext facesContext = FacesContext.getCurrentInstance();		
		UIViewRoot viewRoot = facesContext.getViewRoot();	
		switch (typeComponent) {
		case INPUT_TEXTFIELD:
			HtmlInputText uiComponent = (HtmlInputText) viewRoot.findComponent(updateField);		
			uiComponent.setValue(updateValue);				
			break;
		}
	}
	
	/**
	 * retrieves a UIComponent
	 */
	public static UIComponent getComponent (String formName, String fieldName) {
		FacesContext facesContext = FacesContext.getCurrentInstance();		
		UIViewRoot viewRoot = facesContext.getViewRoot();	
		UIComponent component = viewRoot.findComponent(formName + ":" + fieldName);
		return component;
	}		
	
	/**
	 * retrieves the value from a UIComponent
	 */
	public static Object getComponentValue (String formName, String fieldName) {
		FacesContext facesContext = FacesContext.getCurrentInstance();		
		UIViewRoot viewRoot = facesContext.getViewRoot();	
		UIComponent component = viewRoot.findComponent(formName + ":" + fieldName);
		System.out.println("UGGLE2");
		if (component!=null) {
			System.out.println(component);			
			if (component instanceof HtmlInputText) {
				return ((HtmlInputText)component).getSubmittedValue();
			} else if (component instanceof HtmlSelectOneRadio) {
				return ((HtmlSelectOneRadio)component).getSubmittedValue();
			} else if (component instanceof HtmlSelectOneMenu) {
				Object currVal = ((HtmlSelectOneMenu)component).getSubmittedValue();
				if (currVal==null) {
					currVal = ((HtmlSelectOneMenu)component).getValue(); 
				}
				return currVal;
			} else if (component instanceof HtmlInputSecret) {
				return ((HtmlInputSecret)component).getSubmittedValue();
			}
		}
		return null;
	}	
	
	/**
	 * sets the value for a UIComponent and changes the validation for the component based off
	 * the boolean parameter
	 */
	public static Object setComponentValue (String formName, String fieldName, String value) {
		FacesContext facesContext = FacesContext.getCurrentInstance();		
		UIViewRoot viewRoot = facesContext.getViewRoot();				
		UIComponent component = viewRoot.findComponent(formName + ":" + fieldName);		
		if (component!=null) {			
			if (component instanceof HtmlInputText) {
				((HtmlInputText)component).setSubmittedValue(value);
			} else if (component instanceof HtmlSelectOneRadio) {
				((HtmlSelectOneRadio)component).setSubmittedValue(value);				
			} else if (component instanceof HtmlInputSecret) {
				((HtmlInputSecret)component).setSubmittedValue(value);
			} else if (component instanceof HtmlSelectOneMenu) {
				((HtmlSelectOneMenu)component).setSubmittedValue(value);
			}
		}
		return null;
	}		
	
	
	/**
	 * sets the validation for a component to be required or not
	 */
	public static Object setComponentValidation (String formName, String fieldName, boolean required) {
		FacesContext facesContext = FacesContext.getCurrentInstance();		
		UIViewRoot viewRoot = facesContext.getViewRoot();	
		UIInput component = (UIInput)viewRoot.findComponent(formName + ":" + fieldName);
		if (component!=null) {
			component.setRequired(required);
		}
		return null;
	}	
	
	/**
	 * sets the component to be disabled or not
	 */
	public static void setComponentDisabled (String formName, String fieldName, boolean disabled) {
		FacesContext facesContext = FacesContext.getCurrentInstance();		
		UIViewRoot viewRoot = facesContext.getViewRoot();	

		Object component = (Object)viewRoot.findComponent(formName + ":" + fieldName);
		if (component!=null) {

			if (component instanceof HtmlSelectOneMenu) {
				((HtmlSelectOneMenu)component).setDisabled(disabled);
			} else if (component instanceof HtmlInputText) {
				((HtmlInputText)component).setDisabled(disabled);
			} else if (component instanceof HtmlInputSecret) {
				((HtmlInputSecret)component).setDisabled(disabled);
			}
		}
	}			
	
	/**
	 * retrieves the attributes from a JSF UI Component
	 */
	public static Map getComponentAttributes (String formName, String fieldName) {
		FacesContext facesContext = FacesContext.getCurrentInstance();		
		UIViewRoot viewRoot = facesContext.getViewRoot();	
		UIComponent component = viewRoot.findComponent(formName + ":" + fieldName);
		if (component!=null) {
		     return component.getAttributes();
		}
		return null;
	}
	public static ArrayList<SelectItem> populateAsSelectItem(List<String> inList){
		ArrayList<SelectItem> selectItems = new ArrayList<SelectItem>();
        if( inList != null )
        {
            ListIterator<String> options = inList.listIterator();
            String option=null;
            while (options.hasNext()) {
                option = options.next();
                SelectItem item = new SelectItem();
                item.setValue(option);
                item.setLabel(option);
                selectItems.add(item);
            }
        }
    return selectItems;
	}

	public static ArrayList<SelectItem> populateAsSelectItem(List<String> displayName, List<String> value){
		ArrayList<SelectItem> selectItems = new ArrayList<SelectItem>();
        if( displayName != null )
        {
            ListIterator<String> displayNameOptions = displayName.listIterator();
            ListIterator<String> valueOptions = value.listIterator();
            while (displayNameOptions.hasNext()) {
                SelectItem item = new SelectItem();
                item.setValue(valueOptions.next());
                item.setLabel(displayNameOptions.next());
                selectItems.add(item);
            }
        }
    return selectItems;
	}

	/**
	 * populates array of SelectItem for the given HashMap.
	 * @param nameValue Key is actual value and Value is the display text.
	 * @return
	 */
	public static ArrayList<SelectItem> populateAsSelectItem(Map<String, String> nameValue){
		ArrayList<SelectItem> selectItems = new ArrayList<SelectItem>();
        if( nameValue != null )
        {
            Iterator<String> options = nameValue.keySet().iterator();
            String option;
            while (options.hasNext()) {
                SelectItem item = new SelectItem();
                option=options.next();
                item.setValue(option);
                item.setLabel(nameValue.get(option));
                selectItems.add(item);
            }
        }
    return selectItems;
	}
	

    /**
     * Close up the JSF Faces Context in case of HttpResponse Redirect
     * 
     * @throws IOException
     */
    public static void complementFaceContext()
	    throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();
		context.responseComplete();
	}

}
