<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:import href="../common/keep.xsl" />
    <xsl:strip-space elements="*"/>

    <xsl:template match="/">
        <xsl:apply-templates select="//table"/>
    </xsl:template>

    <xsl:template match="table">
        <countries>
            <xsl:apply-templates select="//tr[td]">
            </xsl:apply-templates>
        </countries>
    </xsl:template>

    <xsl:template match="tr[td]">
        <country>
            <name>
                <xsl:value-of select="td[2]" />
            </name>
            <languages>
                <xsl:apply-templates select="td/tt[@title]" />
            </languages>
        </country>
    </xsl:template>

    <xsl:template match="tt">
        <language>
            <code>
                <xsl:value-of select="text()" />
            </code>
            <name>
                <xsl:value-of select="@title" />
            </name>
        </language>
    </xsl:template>

</xsl:stylesheet>