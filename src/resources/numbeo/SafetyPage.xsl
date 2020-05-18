<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:import href="../common/keep.xsl" />
    <xsl:strip-space elements="*"/>

    <xsl:template match="/">
        <xsl:apply-templates select="//table[@id='t2']"/>
    </xsl:template>

    <xsl:template match="//table[@id='t2']">
        <root>
            <xsl:for-each select="//tr">
                <xsl:variable name="place" select="td[@class='cityOrCountryInIndicesTable']" />
                <place>
                    <xsl:choose>
                        <xsl:when test="contains($place, ',')">
                            <xsl:value-of select="substring-before($place, ',')"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="$place" />
                        </xsl:otherwise>
                    </xsl:choose>
                </place>
                <safety-index><xsl:value-of select="td[last()]" /></safety-index>
            </xsl:for-each>
        </root>
    </xsl:template>

</xsl:stylesheet>