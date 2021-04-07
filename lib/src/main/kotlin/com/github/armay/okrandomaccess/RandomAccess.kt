package com.github.armay.okrandomaccess

/**
 * This interface marks that an implementing object supports random access, i.e. has a mutable position property.
 */
interface RandomAccess {
    /**
     * The current position, at which the next read or write occurs.
     */
    var position: Long
}