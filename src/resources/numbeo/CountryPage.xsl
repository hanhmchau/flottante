<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:import href="../common/keep.xsl" />
    <xsl:strip-space elements="*"/>

    <xsl:template match="/">
        <root>
            <xsl:apply-templates select="//*[@class='cityOrCountryInIndicesTable']//a|//*[@class='small_font']/a[starts-with(@href, 'in/')]"/>
        </root>
    </xsl:template>
</xsl:stylesheet>