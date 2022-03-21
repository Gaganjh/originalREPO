<?xml version="1.0" ?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" omit-xml-declaration="yes"/>

	<xsl:template match="testsuites">
        <table>
            <tr>
                <td>Tests</td><td>Failures</td><td>Errors</td>
            </tr>
            <tr>
                <td>
                <xsl:value-of select="sum(testsuite/@tests)"/>
                </td>
                <td>
                <xsl:value-of select="sum(testsuite/@failures)"/>
                </td>
                <td>
                <xsl:value-of select="sum(testsuite/@errors)"/>
                </td>
            </tr>
        </table>
    </xsl:template>

</xsl:stylesheet>
