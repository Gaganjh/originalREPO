package com.manulife.pension.bd.web.bob.planReview;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Miscellaneous;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.service.planReview.valueobject.PlanReviewCoverImageDetails;
import com.manulife.pension.util.content.manager.ContentCacheManager;

public class CoverPageImage extends PlanReviewCoverImageDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final Logger logger = Logger.getLogger(CoverPageImage.class);

	private String lowResolutionImagePath = StringUtils.EMPTY;
	private String highResolutionImagePath = StringUtils.EMPTY;
	private String coverPageCaptionCmaValue = StringUtils.EMPTY;
	private String onlineCoverImagePath = StringUtils.EMPTY;

	public CoverPageImage(PlanReviewCoverImageDetails coverPageImageDetails) {
		super();
		super.setCoverPageCaptionCmaKey(coverPageImageDetails
				.getCoverPageCaptionCmaKey());
		super.setDefaultCoverPageImage(coverPageImageDetails
				.isDefaultCoverPageImage());
		super.setHighResolutionImageCmaKey(coverPageImageDetails
				.getHighResolutionImageCmaKey());
		super.setHighResolutionUNCPath(coverPageImageDetails
				.getHighResolutionUNCPath());
		super.setLowResolutionImageCmaKey(coverPageImageDetails
				.getLowResolutionImageCmaKey());
		super.setLowResolutionUNCPath(coverPageImageDetails
				.getLowResolutionUNCPath());
		super.setOrderIndex(coverPageImageDetails.getOrderIndex());
		super.setOnlineCoverImageCmaKey(coverPageImageDetails
				.getOnlineCoverImageCmaKey());
	}
	
	public String getOnlineCoverImagePath() {

		if (StringUtils.isBlank(onlineCoverImagePath)) {
			
			Miscellaneous content = null;
			try {
				content = (Miscellaneous) ContentCacheManager.getInstance()
						.getContentById(super.getOnlineCoverImageCmaKey(),
								ContentTypeManager.instance().MISCELLANEOUS,
								null);
			} catch (ContentException contentException) {
				logger.error("Error occured while retrieveing CMA Content Key : "
						+ super.getLowResolutionImageCmaKey()
						+ " : "
						+ contentException);
			}

			if (content != null) {

				if (content.getImage() == null) {
					logger.error("Image should not be null for Content Key : "
							+ super.getOnlineCoverImageCmaKey());
				} else {

					onlineCoverImagePath = content.getImage().getPath();
				}
			}
		}

		return onlineCoverImagePath;
	}

	public String getLowResolutionImagePath() {

		if (StringUtils.isBlank(lowResolutionImagePath)) {
			
			Miscellaneous content = null;
			try {
				content = (Miscellaneous) ContentCacheManager.getInstance()
						.getContentById(super.getLowResolutionImageCmaKey(),
								ContentTypeManager.instance().MISCELLANEOUS,
								null);
			} catch (ContentException contentException) {
				logger.error("Error occured while retrieveing CMA Content Key : "
						+ super.getLowResolutionImageCmaKey()
						+ " : "
						+ contentException);
			}

			if (content != null) {

				if (content.getImage() == null) {
					logger.error("Image should not be null for Content Key : "
							+ super.getLowResolutionImageCmaKey());
				} else {

					lowResolutionImagePath = content.getImage().getPath();
				}
			}
		}

		return lowResolutionImagePath;
	}

	public String getHighResolutionImagePath() {

		if (StringUtils.isBlank(highResolutionImagePath)) {

			Miscellaneous content = null;
			try {
				content = (Miscellaneous) ContentCacheManager.getInstance()
						.getContentById(super.getHighResolutionImageCmaKey(),
								ContentTypeManager.instance().MISCELLANEOUS,
								null);
			} catch (ContentException contentException) {
				logger.error("Error occured while retrieveing CMA Content Key : "
						+ super.getHighResolutionImageCmaKey()
						+ " : "
						+ contentException);
			}

			if (content != null) {
				
				if (content.getImage() == null) {
					logger.error("Image should not be null for Content Key : "
							+ super.getHighResolutionImageCmaKey());
				} else {

					highResolutionImagePath = content.getImage().getPath();
				}
			}
		}
		return highResolutionImagePath;
	}

	public String getCoverPageCaptionCmaValue() {

		if (StringUtils.isBlank(coverPageCaptionCmaValue)) {

			coverPageCaptionCmaValue = ContentHelper.getContentText(
					super.getCoverPageCaptionCmaKey(),
					ContentTypeManager.instance().MISCELLANEOUS, null);
		}

		return coverPageCaptionCmaValue;
	}

}
