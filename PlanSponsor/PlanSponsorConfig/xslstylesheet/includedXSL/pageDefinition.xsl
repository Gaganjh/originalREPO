<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

	<xsl:template name="pageDefinition">

		<fo:layout-master-set>
			<fo:simple-page-master margin-bottom="1.0cm" margin-left="0.5cm" margin-right="0.5cm" margin-top="1.0cm" master-name="pageLandscapeLayout" page-height="27.94cm" page-width="21.59cm">
				<fo:region-body margin-bottom="0.5cm" margin-top="0cm"/>
				<fo:region-before extent="0cm"/>
				<fo:region-after extent="0.5cm"/>
			</fo:simple-page-master>
		</fo:layout-master-set>
		
	</xsl:template>
		
</xsl:stylesheet>