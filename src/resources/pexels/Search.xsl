<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:import href="../common/keep.xsl" />
    <xsl:strip-space elements="*"/>

    <xsl:template match="/">
        <images>
            <xsl:apply-templates select="//*[@class='photo-item__img']"/>
        </images>
    </xsl:template>

    <xsl:template match="//*[@class='photo-item__img']">
        <image>
            <xsl:value-of select="substring-before(@data-big-src, '?')"/>
        </image>
    </xsl:template>
</xsl:stylesheet>