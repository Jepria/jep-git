package com.technology.jep.jepria.client.widget.field.multistate.large;

import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_CONTENT_DISPOSITION_INLINE;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_CONTENT_DISPOSITION;

import com.google.gwt.user.client.ui.Image;

@SuppressWarnings("unchecked")
public class JepImageField extends JepLargeField<Image> {

	public JepImageField(){
		this("");
	}
	
	public JepImageField(String labelText) {
		super(labelText);	
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addViewCard() {
		viewCard = new Image();
		viewPanel.add(viewCard);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setViewValue(Object reference) {
		String downloadUrl = buildDownloadUrl(reference);
		if(downloadUrl != null) {
			viewCard.setUrl(downloadUrl);
		} else {
			viewCard.setUrl("");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String buildDownloadUrl(Object reference) {
		String downloadUrl = super.buildDownloadUrl(reference);
		StringBuilder sbRef = new StringBuilder();
	
		if(downloadUrl != null) {
			sbRef.append(downloadUrl);
			sbRef.append("&");
			sbRef.append(DOWNLOAD_CONTENT_DISPOSITION);
			sbRef.append("=");
			sbRef.append(DOWNLOAD_CONTENT_DISPOSITION_INLINE);
		} else {
			return null;
		}
		
		return sbRef.toString();
	}
}
