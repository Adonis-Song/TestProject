package com.arthenica.mysongapplication.playerandvoice

import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.audio.AudioProcessor
import com.google.android.exoplayer2.audio.AudioProcessor.EMPTY_BUFFER
import java.nio.ByteBuffer
import java.nio.ByteOrder

class MyAudioProcessor : AudioProcessor {
    private val TAG = "MyAudioProcessor"
    private var m_channelCnt = Format.NO_VALUE
    private var m_sampleRate = Format.NO_VALUE
    private var m_encoding = C.ENCODING_INVALID
    private var m_inputEnd = false
    private var m_buffer: ByteBuffer = EMPTY_BUFFER
    private var m_outputBuffer: ByteBuffer = EMPTY_BUFFER
    private var volume: Array<Float> = Array(8, { _ -> 1.0f}) // 控制具体每个频道的音量大小

    fun SetVolume(idx: Int, vol: Float){
        if(idx >= volume.size)
            throw IllegalArgumentException()
        volume[idx] = vol
    }

    fun GetVolume(idx: Int): Float{
        if(idx >= volume.size)
            throw IllegalArgumentException()
        return volume[idx]
    }

    override fun isActive(): Boolean {
        return m_encoding != C.ENCODING_INVALID
    }

    override fun queueEndOfStream() {
        m_inputEnd = true
    }

    override fun configure(inputAudioFormat: AudioProcessor.AudioFormat): AudioProcessor.AudioFormat {
        m_channelCnt = inputAudioFormat.channelCount
        m_sampleRate = inputAudioFormat.sampleRate
        m_encoding = inputAudioFormat.encoding;
        return inputAudioFormat
    }

    override fun flush() {
        m_outputBuffer = EMPTY_BUFFER
        m_inputEnd = false
    }

    override fun queueInput(buffer: ByteBuffer) {
        //最后只输出立体声混音结果
        var position = buffer.position()
        val limit = buffer.limit()
        val size = limit - position

        if (m_buffer.capacity() < size)
            m_buffer = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder())
        else
            m_buffer.clear()

        when(m_encoding){
            C.ENCODING_PCM_16BIT->{
                while(position < limit){
                    var retL = 0.0f
                    var retR = 0.0f
                    for(i in 0..m_channelCnt - 1){
                        if(i%2 == 0)
                            retL += buffer.getShort(position + i * 2) * volume[i]
                        else
                            retR += buffer.getShort(position + i * 2) * volume[i]
                    }
                    position += m_channelCnt * 2

                    m_buffer.putShort((retL / m_channelCnt).toShort())
                    m_buffer.putShort((retR / m_channelCnt).toShort())
                }
            }
            else->{
                throw IllegalStateException()
            }
        }
        buffer.position(limit)
        m_buffer.flip()
        m_outputBuffer = m_buffer
    }

    override fun isEnded(): Boolean {
        return m_inputEnd && m_outputBuffer === AudioProcessor.EMPTY_BUFFER
    }

    override fun getOutput(): ByteBuffer {
        val outputBuffer = m_outputBuffer
        m_outputBuffer = EMPTY_BUFFER
        return outputBuffer
    }

    override fun reset() {
        flush()
        m_buffer = AudioProcessor.EMPTY_BUFFER
        m_sampleRate = Format.NO_VALUE
        m_channelCnt = Format.NO_VALUE
        m_encoding = C.ENCODING_INVALID
    }


}