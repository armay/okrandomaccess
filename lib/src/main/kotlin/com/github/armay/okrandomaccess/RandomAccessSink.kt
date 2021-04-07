package com.github.armay.okrandomaccess

import okio.Sink

/**
 * This interface equips Okio sink with random access functionality.
 */
interface RandomAccessSink : RandomAccess, Sink