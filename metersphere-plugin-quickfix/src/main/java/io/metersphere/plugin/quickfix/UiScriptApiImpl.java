package io.metersphere.plugin.quickfix;
import io.metersphere.plugin.core.api.UiScriptApi;
import io.metersphere.plugin.core.ui.PluginResource;
import io.metersphere.plugin.core.ui.UiScript;
import io.metersphere.plugin.core.utils.LogUtil;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class UiScriptApiImpl extends UiScriptApi {

    /**
     * 企业版插件增加 这个方法
     *
     * @return 是否是企业版插件
     */
    public boolean xpack() {
        return true;
    }

    @Override
    public PluginResource init() {
        LogUtil.info("开始初始化脚本内容 ");
        List<UiScript> uiScripts = new LinkedList<>();

        //quickfix  sampler
        String script = getJson("/json/ui-quickfix.json");
        UiScript uiScript = new UiScript("QuickFixSampler", "quickfix 请求", "io.metersphere.plugin.quickfix.sampler.MsQuickFixSampler", script);
        uiScript.setJmeterClazz("AbstractSampler");
        uiScript.setFormOption(getJson("/json/ui-quickfix_form.json"));
        uiScripts.add(uiScript);


        LogUtil.info("初始化脚本内容结束 ");
        return new PluginResource("quickfix-v1.1.0", uiScripts);
    }

    @Override
    public String customMethod(String req) {
        LogUtil.info("进入自定义方法 ,开始写自己的逻辑吧");
        return null;
    }

    public String getJson(String path) {
        try {
            InputStream in = UiScriptApiImpl.class.getResourceAsStream(path);
            String json = org.apache.commons.io.IOUtils.toString(in);
            return json;
        } catch (Exception ex) {
            LogUtil.error(ex.getMessage());
        }
        return null;
    }

    /**
     * 测试用
     *
     * @return
     */
    public static String test() {
        return "success";
    }
}