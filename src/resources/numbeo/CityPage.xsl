<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:import href="../common/keep.xsl" />
    <xsl:strip-space elements="*"/>

    <xsl:template match="/">
        <xsl:apply-templates select="//*[@class='data_wide_table']"/>
    </xsl:template>

    <xsl:template match="//*[contains(@class, 'priceValue')]">
        <td class="priceValue">
            <xsl:choose>
                <xsl:when test="contains(text(), '&amp;')">
                    <xsl:value-of select="translate(substring-before(text(), '&amp;'), ',', '')" />
                </xsl:when>
                <xsl:when test="contains(text(), '?')">
                    0
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="translate(text(), ',', '')" />
                </xsl:otherwise>
            </xsl:choose>
        </td>
    </xsl:template>

    <xsl:template match="//td[position() = 1]">
        <td class="title">
            <xsl:value-of select="." />
        </td>
    </xsl:template>

    <xsl:template match="//tr[td]">
        <tr class="row" category="{count(preceding-sibling::tr[th])}">
            <xsl:apply-templates />
        </tr>
    </xsl:template>

    <xsl:template match="//tr[th]">
        <tr class="category-wrapper" group="{count(preceding-sibling::tr[th]) + 1}">
            <xsl:apply-templates />
        </tr>
    </xsl:template>

    <xsl:template match="//*[contains(@class, 'highlighted_th prices')]">
        <th>
            <xsl:value-of select="." />
        </th>
    </xsl:template>

    <xsl:template match="//*[contains(@class, 'priceBarTd')]|//th[not(contains(@class, 'highlighted_th prices'))]"/>

</xsl:stylesheet>