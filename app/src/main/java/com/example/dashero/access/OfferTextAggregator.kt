package com.example.dashero.access

import android.view.accessibility.AccessibilityNodeInfo

object OfferTextAggregator {

    fun collectText(root: AccessibilityNodeInfo?): String {
        if (root == null) return ""
        val sb = StringBuilder()
        dfs(root, sb)
        return sb.toString()
    }

    private fun dfs(node: AccessibilityNodeInfo, sb: StringBuilder) {
        val t = node.text?.toString()?.trim()
        if (!t.isNullOrBlank()) sb.appendLine(t)

        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            dfs(child, sb)
        }
    }
}
