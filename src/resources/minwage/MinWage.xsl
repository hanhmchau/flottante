<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:import href="../common/keep.xsl" />
    <xsl:strip-space elements="*"/>

    <xsl:template match="/">
        <xsl:apply-templates select="//table[@id='tb1']"/>
    </xsl:template>

    <xsl:template match="//table[@id='tb1']">
        <root>
            <xsl:for-each select="//tr[position() > 1]">
                <country>
                    <name>
                        <xsl:value-of select="normalize-space(substring-before(td[1]//a/text(), '['))" />
                    </name>
                    <wage>
                        <xsl:value-of select="td[5]/@data-value" />
                    </wage>
                </country>
            </xsl:for-each>
        </root>
    </xsl:template>

</xsl:stylesheet>