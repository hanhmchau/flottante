<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">
        <cost-of-living>
            <categories>
                <xsl:apply-templates select="//*[@class='category-wrapper']" />
            </categories>
        </cost-of-living>
    </xsl:template>

    <xsl:template match="//*[@class='category-wrapper']">
        <xsl:variable name="group" select="@group" />
        <entry>
            <key>
                <xsl:value-of select="th" />
            </key>
            <value>
                <items>
                    <xsl:apply-templates select="../tr[@category = $group]" />
                </items>
            </value>
        </entry>
    </xsl:template>

    <xsl:template match="//tr[@category]">
        <item>
            <xsl:apply-templates />
        </item>
    </xsl:template>

    <xsl:template match="//td">
        <xsl:variable name="pos" select="position()"/>
        <xsl:choose>
            <xsl:when test="$pos = 1">
                <title><xsl:value-of select="." /></title>
            </xsl:when>
            <xsl:when test="$pos = 2">
                <value><xsl:value-of select="." /></value>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>