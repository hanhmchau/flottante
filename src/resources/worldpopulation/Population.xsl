<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:import href="../common/keep.xsl" />
    <xsl:strip-space elements="*"/>

    <xsl:template match="/">
        <xsl:apply-templates select="//table"/>
    </xsl:template>

    <xsl:template match="//table">
        <root>
            <xsl:for-each select="//tr">
                <xsl:variable name="population" select="td[position() = 3]/text()" />
                <city>
                    <xsl:value-of select="td[position() = 2]/a"/>
                </city>
                <population>
                    <xsl:value-of select="translate($population, ',', '')"/>
                </population>
            </xsl:for-each>
        </root>
    </xsl:template>

</xsl:stylesheet>