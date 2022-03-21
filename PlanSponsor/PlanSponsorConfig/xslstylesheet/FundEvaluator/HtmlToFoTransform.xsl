<?xml version="1.0"?>
<xsl:stylesheet 
  version="1.0" 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:fo="http://www.w3.org/1999/XSL/Format">
  
<xsl:template match="b">
  <fo:inline font-weight="bold">
    <xsl:apply-templates select="*|text()"/>
  </fo:inline>
</xsl:template>

<xsl:template match="strong">
  <fo:inline font-weight="bold">
    <xsl:apply-templates select="*|text()"/>
  </fo:inline>
</xsl:template>

<xsl:template match="u">
  <fo:inline text-decoration="underline">
    <xsl:apply-templates select="*|text()"/>
  </fo:inline>
</xsl:template>

<xsl:template match="p">
  <fo:block>
    <xsl:apply-templates select="*|text()"/>
  </fo:block>
</xsl:template>

<xsl:template match="br">
  <fo:block padding-before="0.1cm">
    <xsl:apply-templates select="*|text()"/>
  </fo:block>
</xsl:template>

<xsl:template match="i">
  <fo:inline font-style="italic">
    <xsl:apply-templates select="*|text()"/>
  </fo:inline>
</xsl:template>

<xsl:template match="sup">
  <fo:inline vertical-align="super">
    <xsl:apply-templates select="*|text()"/>
  </fo:inline>
</xsl:template>
          
<xsl:template match="center">
  <fo:block text-align="center">
    <xsl:apply-templates select="*|text()"/>
  </fo:block>
</xsl:template>
  
</xsl:stylesheet>

