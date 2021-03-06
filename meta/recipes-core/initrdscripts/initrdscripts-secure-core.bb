SUMMARY = "Basic init for initramfs to mount and pivot root"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "\
    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420 \
"

SRC_URI = "\
    file://init \
"

do_install() {
    install -m 0755 "${WORKDIR}/init" "${D}/init"

    # Create device nodes expected by kernel in initramfs
    # before executing /init.
    install -d "${D}/dev"
    mknod -m 0600 "${D}/dev/console" c 5 1
}

FILES_${PN} = "\
    /init \
    /dev \
"

# Install the minimal stuffs only, and don't care how the external
# environment is configured.

# @coreutils: echo, cat, sleep, switch_root, expr, mkdir
# @util-linux: mount
# @grep: grep
# @gawk: awk
# @eudev or udev: udevd, udevadm
RDEPENDS_${PN} += "\
    coreutils \
    util-linux-mount \
    grep \
    gawk \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'udev', 'eudev', d)} \
"

# @initrdscripts-ima: init.ima
# @cryptfs-tpm2-initramfs: init.cryptfs
RRECOMMENDS_${PN} += "\
    initrdscripts-ima \
    cryptfs-tpm2-initramfs \
"
