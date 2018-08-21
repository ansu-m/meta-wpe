SUMMARY = "WPE Framework common plugins"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e3fc50a88d0a364313df4b21ef20c29e"
PR = "r1"

require wpeframework-plugins.inc

SRC_URI = "git://github.com/WebPlatformForEmbedded/WPEFrameworkPlugins.git;protocol=git;branch=master \
           file://index.html \
           file://0003-RemoteControl-fix-refsw-include-path.patch \
           "

SRCREV = "dec5571735ab6786e3765b5b18d5c11631e332f3"

WEBKITBROWSER_AUTOSTART ?= "true"
WEBKITBROWSER_MEDIADISKCACHE ?= "false"
WEBKITBROWSER_MEMORYPRESSURE ?= "databaseprocess:50m,networkprocess:100m,webprocess:300m,rpcprocess:50m"
WEBKITBROWSER_MEMORYPROFILE ?= "128m"
WEBKITBROWSER_MSEBUFFERS ?= "audio:2m,video:15m,text:1m"
WEBKITBROWSER_STARTURL ?= "http://localhost:8080/index.html"
WEBKITBROWSER_USERAGENT ?= "Mozilla/5.0 (Macintosh, Intel Mac OS X 10_11_4) AppleWebKit/602.1.28+ (KHTML, like Gecko) Version/9.1 Safari/601.5.17"
WEBKITBROWSER_DISKCACHE ?= "0"
WEBKITBROWSER_XHRCACHE ?= "false"
WEBKITBROWSER_TRANSPARENT ?= "false"
WEBKITBROWSER_THREADEDPAINTING ?= "1"

WPEFRAMEWORK_LOCATIONSYNC_URI ?= "http://jsonip.metrological.com/?maf=true"
WPEFRAMEWORK_REMOTECONTROL_NEXUS_IRMODE ?= "16"
WPEFRAMEWORK_REMOTECONTROL_KEYMAP = "OSMCKeyMap.json"

WPEFRAMEWORK_PLUGIN_WEBSERVER_PORT ?= "8080"
WPEFRAMEWORK_PLUGIN_WEBSERVER_PATH ?= "/var/www/"

REMOTECONTROL_RF4CE_NAME ?= "GreenPeak"
REMOTECONTROL_RF4CE_REMOTE_ID ?= "GPSTB"
REMOTECONTROL_RF4CE_MODULE ?= "/lib/modules/gpK5.ko"
REMOTECONTROL_RF4CE_NODE_ID ?= "249"
REMOTECONTROL_RF4CE_DEPENDENCY ?= "rf4ce-hal-gp"


WPEFRAMEWORK_PLUGIN_COMPOSITOR_HARDWARE_READY ?= "0"

# Snapshot only works on BRCM STBs and RPIs
WPE_SNAPSHOT ?= ""
WPE_SNAPSHOT_rpi = "snapshot"
WPE_SNAPSHOT_nexus = "snapshot"
WPE_SNAPSHOT_DEP = "${@bb.utils.contains('PREFERRED_PROVIDER_virtual/egl', 'broadcom-refsw', 'broadcom-refsw', 'userland', d)}"

## Compositor settings, if Wayland is in the distro set the implementation to Wayland with Westeros dependency
WPE_COMPOSITOR ?= "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'compositor', '', d)}"
WPE_COMPOSITOR_IMPL ?= "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'Wayland', 'None', d)}"
WPE_COMPOSITOR_DEP ?= "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'westeros', '', d)}"
WPE_COMPOSITOR_EXTRAFLAGS ?= ""

# PACAKAGE CONFIG
PACKAGECONFIG ?= " \
    ${WPE_SNAPSHOT} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'bluetooth', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wpeframework', '${WPE_COMPOSITOR} network', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'wifi', 'network wifi', '', d)} \
    deviceinfo dictionary locationsync monitor remote remote-uinput timesync tracing ux virtualinput webkitbrowser webserver youtube \
"

PACKAGECONFIG[bluetooth]      = "-DWPEFRAMEWORK_PLUGIN_BLUETOOTH=ON -DWPEFRAMEWORK_PLUGIN_BLUETOOTH_AUTOSTART=false,-DWPEFRAMEWORK_PLUGIN_BLUETOOTH=OFF,,dbus-glib bluez5"
PACKAGECONFIG[compositor]     = "-DWPEFRAMEWORK_PLUGIN_COMPOSITOR=ON -DWPEFRAMEWORK_PLUGIN_COMPOSITOR_HARDWAREREADY=${WPEFRAMEWORK_PLUGIN_COMPOSITOR_HARDWARE_READY} -DWPEFRAMEWORK_PLUGIN_COMPOSITOR_IMPLEMENTATION=${WPE_COMPOSITOR_IMPL} -DWPEFRAMEWORK_PLUGIN_COMPOSITOR_VIRTUALINPUT=ON ${WPE_COMPOSITOR_EXTRAFLAGS},-DWPEFRAMEWORK_PLUGIN_COMPOSITOR=OFF,${WPE_COMPOSITOR_DEP}"
PACKAGECONFIG[deviceinfo]     = "-DWPEFRAMEWORK_PLUGIN_DEVICEINFO=ON,-DWPEFRAMEWORK_PLUGIN_DEVICEINFO=OFF,"
PACKAGECONFIG[dictionary]     = "-DWPEFRAMEWORK_PLUGIN_DICTIONARY=ON,-DWPEFRAMEWORK_PLUGIN_DICTIONARY=OFF,"
PACKAGECONFIG[locationsync]   = "-DWPEFRAMEWORK_PLUGIN_LOCATIONSYNC=ON \
   -DWPEFRAMEWORK_PLUGIN_LOCATIONSYNC_URI=${WPEFRAMEWORK_LOCATIONSYNC_URI} \
   ,-DWPEFRAMEWORK_PLUGIN_LOCATIONSYNC=OFF,"
PACKAGECONFIG[network]        = "-DWPEFRAMEWORK_PLUGIN_NETWORKCONTROL=ON,-DWPEFRAMEWORK_PLUGIN_NETWORKCONTROL=OFF,"
PACKAGECONFIG[monitor]        = "-DWPEFRAMEWORK_PLUGIN_MONITOR=ON \
                                 -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_MEMORYLIMIT=614400 \
                                 -DWPEFRAMEWORK_PLUGIN_YOUTUBE_MEMORYLIMIT=614400 \
                                 -DWPEFRAMEWORK_PLUGIN_NETFLIX_MEMORYLIMIT=307200 \
                                ,-DWPEFRAMEWORK_PLUGIN_MONITOR=OFF,"
PACKAGECONFIG[opencdmi]       = "-DWPEFRAMEWORK_PLUGIN_OPENCDMI=ON \
                                 -DWPEFRAMEWORK_PLUGIN_OPENCDMI_AUTOSTART=true \
                                 -DWPEFRAMEWORK_PLUGIN_OPENCDMI_OOP=true \
                                ,,"
PACKAGECONFIG[opencdmi_ck]    = "-DPLUGIN_OPENCDMI_CLEARKEY=ON,,"
PACKAGECONFIG[opencdmi_pr]    = "-DPLUGIN_OPENCDMI_PLAYREADY=ON,,"
PACKAGECONFIG[opencdmi_prnx]  = "-DPLUGIN_OPENCDMI_PLAYREADY_NEXUS=ON,,"
PACKAGECONFIG[opencdmi_wv]    = "-DPLUGIN_OPENCDMI_WIDEVINE=ON,,"
PACKAGECONFIG[remote]         = "-DWPEFRAMEWORK_PLUGIN_REMOTECONTROL=ON \
                                 -DWPEFRAMEWORK_PLUGIN_REMOTECONTROL_KEYMAP=${WPEFRAMEWORK_REMOTECONTROL_KEYMAP} \
                                ,-DWPEFRAMEWORK_PLUGIN_REMOTECONTROL=OFF,"
PACKAGECONFIG[remote-nexus]   = "-DWPEFRAMEWORK_PLUGIN_REMOTECONTROL_IRNEXUS=ON \
                                 -DWPEFRAMEWORK_PLUGIN_REMOTECONTROL_IRMODE=${WPEFRAMEWORK_REMOTECONTROL_IRMODE} \
                                ,-DWPEFRAMEWORK_PLUGIN_REMOTECONTROL_IRNEXUS=OFF,broadcom-refsw"
PACKAGECONFIG[remote-uinput]  = "-DWPEFRAMEWORK_PLUGIN_REMOTECONTROL_DEVINPUT=ON,-DWPEFRAMEWORK_PLUGIN_REMOTECONTROL_DEVINPUT=OFF,"
PACKAGECONFIG[remote-gp]      = "-DWPEFRAMEWORK_PLUGIN_REMOTECONTROL_RFCE_NAME=${REMOTECONTROL_RF4CE_NAME} \
				 -DWPEFRAMEWORK_PLUGIN_REMOTECONTROL_RFCE_REMOTE_ID=${REMOTECONTROL_RF4CE_REMOTE_ID} \ 
				 -DWPEFRAMEWORK_PLUGIN_REMOTECONTROL_RFCE_MODULE=${REMOTECONTROL_RF4CE_MODULE} \
				 -DWPEFRAMEWORK_PLUGIN_REMOTECONTROL_RFCE_NODE_ID=${REMOTECONTROL_RF4CE_NODE_ID} \
				 ,,${REMOTECONTROL_RF4CE_DEPENDENCY}"
PACKAGECONFIG[snapshot]       = "-DWPEFRAMEWORK_PLUGIN_SNAPSHOT=ON,-DWPEFRAMEWORK_PLUGIN_SNAPSHOT=OFF,${WPE_SNAPSHOT_DEP} libpng"
PACKAGECONFIG[timesync]       = "-DWPEFRAMEWORK_PLUGIN_TIMESYNC=ON,-DWPEFRAMEWORK_PLUGIN_TIMESYNC=OFF,"
PACKAGECONFIG[tracing]        = "-DWPEFRAMEWORK_PLUGIN_TRACECONTROL=ON,-DWPEFRAMEWORK_PLUGIN_TRACECONTROL=OFF,"
PACKAGECONFIG[ux]             = "-DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_UX=ON,-DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_UX=ON,"
PACKAGECONFIG[virtualinput]   = "-DWPEFRAMEWORK_PLUGIN_COMPOSITOR_VIRTUALINPUT=ON,-DWPEFRAMEWORK_PLUGIN_COMPOSITOR_VIRTUALINPUT=OFF,"
PACKAGECONFIG[webkitbrowser]  = "-DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER=ON \
   -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_AUTOSTART="${WEBKITBROWSER_AUTOSTART}" \
   -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_MEDIADISKCACHE="${WEBKITBROWSER_MEDIADISKCACHE}" \
   -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_MEMORYPRESSURE="${WEBKITBROWSER_MEMORYPRESSURE}" \
   -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_MEMORYPROFILE="${WEBKITBROWSER_MEMORYPROFILE}" \
   -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_MSEBUFFERS="${WEBKITBROWSER_MSEBUFFERS}" \
   -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_STARTURL="${WEBKITBROWSER_STARTURL}" \
   -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_USERAGENT="${WEBKITBROWSER_USERAGENT}" \
   -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_DISKCACHE="${WEBKITBROWSER_DISKCACHE}" \
   -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_XHRCACHE="${WEBKITBROWSER_XHRCACHE}" \
   -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_TRANSPARENT="${WEBKITBROWSER_TRANSPARENT}" \
   -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_THREADEDPAINTING="${WEBKITBROWSER_THREADEDPAINTING}" \
   ,-DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER=OFF,wpewebkit"
PACKAGECONFIG[webproxy]       = "-DWPEFRAMEWORK_PLUGIN_WEBPROXY=ON,-DWPEFRAMEWORK_PLUGIN_WEBPROXY=OFF,"
PACKAGECONFIG[webserver]      = "-DWPEFRAMEWORK_PLUGIN_WEBSERVER=ON \
    -DWPEFRAMEWORK_WEBSERVER_PORT="${WPEFRAMEWORK_PLUGIN_WEBSERVER_PORT}" \
    -DWPEFRAMEWORK_WEBSERVER_PATH="${WPEFRAMEWORK_PLUGIN_WEBSERVER_PATH}" \
    ,-DWPEFRAMEWORK_PLUGIN_WEBSERVER=OFF,"
PACKAGECONFIG[webshell]       = "-DWPEFRAMEWORK_PLUGIN_WEBSHELL=ON,-DWPEFRAMEWORK_PLUGIN_WEBSHELL=OFF,"
PACKAGECONFIG[wifi]           = "-DWPEFRAMEWORK_PLUGIN_WIFICONTROL=ON,-DWPEFRAMEWORK_PLUGIN_WIFICONTROL=OFF,,wpa-supplicant"
PACKAGECONFIG[wifi_rdkhal]    = "-DWPEFRAMEWORK_PLUGIN_USE_RDK_HAL_WIFI=ON,-DWPEFRAMEWORK_PLUGIN_USE_RDK_HAL_WIFI=OFF,,wifi-hal"
PACKAGECONFIG[youtube]        = "-DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_YOUTUBE=ON, -DWPEFRAMEWORK_PLUGIN_WEBKITBROWSER_YOUTUBE=OFF,,"
PACKAGECONFIG[systemdconnector] = "-DWPEFRAMEWORK_PLUGIN_SYSTEMDCONNECTOR=ON,-DWPEFRAMEWORK_PLUGIN_SYSTEMDCONNECTOR=OFF,"

EXTRA_OECMAKE += " \
    -DBUILD_REFERENCE=${SRCREV} \
    -DBUILD_SHARED_LIBS=ON \
"
do_install_append() {
    if ${@bb.utils.contains("PACKAGECONFIG", "webserver", "true", "false", d)}
    then
      if ${@bb.utils.contains("PACKAGECONFIG", "webkitbrowser", "true", "false", d)}
      then
          install -d ${D}/var/www
          install -m 0755 ${WORKDIR}/index.html ${D}/var/www/
      fi
      install -d ${D}${WPEFRAMEWORK_PLUGIN_WEBSERVER_PATH}
    fi
}

# ----------------------------------------------------------------------------

FILES_SOLIBSDEV = ""
FILES_${PN} += "${libdir}/wpeframework/plugins/*.so ${libdir}/*.so ${datadir}/WPEFramework/* /var/www/index.html"

INSANE_SKIP_${PN} += "libdir staticdev dev-so"
INSANE_SKIP_${PN}-dbg += "libdir"
