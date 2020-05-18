<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:import href="../common/keep.xsl" />
    <xsl:strip-space elements="*"/>

    <xsl:template match="/">
        <xsl:apply-templates select="//table"/>
    </xsl:template>

    <xsl:template match="table">
        <root>
            <xsl:apply-templates select="//tr">
            </xsl:apply-templates>
        </root>
    </xsl:template>
    
    <xsl:template match="tr">
        <language>
            <name>
                <xsl:value-of select="td[1]" />
            </name>
            <code>
                <xsl:value-of select="td[2]" />
            </code>
        </language>
    </xsl:template>

</xsl:stylesheet>