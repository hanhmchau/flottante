<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" indent="yes"/>

    <xsl:template match="node()|@*">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="/">
        <div class="city-list">
            <xsl:apply-templates select="//city"/>
        </div>
    </xsl:template>

    <xsl:template match="//city">
        <xsl:variable name="coverImage" select="coverImage" />
        <div class="city" style="background-image: url({$coverImage})">
            <xsl:value-of select="name" />
        </div>
    </xsl:template>

</xsl:stylesheet>