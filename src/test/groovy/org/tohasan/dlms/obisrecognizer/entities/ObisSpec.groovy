package org.tohasan.dlms.obisrecognizer.entities

import com.sun.media.sound.InvalidFormatException
import spock.lang.Specification

class ObisSpec extends Specification {

    def "should throw exception if obis parts are separated non-dot character"() {
        when:
        new Obis("1,17,0,18,2,255")

        then:
        thrown InvalidFormatException
    }

    def "should throw exception if obis has less than 6 groups"() {
        when:
        new Obis("1.17.0.18.2")

        then:
        thrown InvalidFormatException
    }

    def "should throw exception if obis has more than 6 groups"() {
        when:
        new Obis("1.17.0.18.2.255.100")

        then:
        thrown InvalidFormatException
    }

    def "should return list of group ids"() {
        given:
        def obis = new Obis('1.17.0.18.2.255')

        expect:
        obis.getGroupIds() == [1, 17, 0, 18, 2, 255]
    }
}
