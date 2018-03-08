package org.tohasan.dlms.obisrecognizer.entities

import com.sun.media.sound.InvalidFormatException
import spock.lang.Specification

class CosemDefinitionSpec extends Specification {

    def "constructor: should throw exception if it gets invalid group count"() {
        when:
        new CosemDefinition([new CosemRangeGroup("0-64")])

        then:
        thrown InvalidFormatException
    }

    def "getGroups: should fill groups when create instance"() {
        given:
        def definition = new CosemDefinition([
                new CosemRangeGroup('1', 'Electricity'),
                new CosemRangeGroup('0-63', 'Ch. $B'),
                new CosemRangeGroup('1-92', 'L3 Current'),
                new CosemRangeGroup('0', 'Billing period avg.'),
                new CosemRangeGroup('0-63', 'Rate $E (20 is total)'),
                new CosemRangeGroup('151,255', 'Last billing period(s)')
        ])

        expect:
        definition.getGroups().size() == 6
    }

    def "getDescription: should return description as group descriptions concatenated by comma"() {
        given:
        def definition = new CosemDefinition([
                new CosemRangeGroup('1', 'Electricity'),
                new CosemRangeGroup('0-63', 'Ch. $B'),
                new CosemRangeGroup('1-92', 'L3 Current'),
                new CosemRangeGroup('0', 'Billing period avg.'),
                new CosemRangeGroup('0-63', 'Rate $E (20 is total)'),
                new CosemRangeGroup('151,255', 'Last billing period(s)')
        ])
        def obis = new Obis('1.48.7.0.18.151')

        expect:
        definition.getDescription(obis) == 'Electricity, Ch. 48, L3 Current, Billing period avg., Rate 18 (20 is total), Last billing period(s)'
    }

    def "getDescription: should exclude groups with empty description when build description"() {
        given:
        def definition = new CosemDefinition([
                new CosemRangeGroup('1', 'Electricity'),
                new CosemRangeGroup('0-63', 'Ch. $B'),
                new CosemRangeGroup('1-92', 'L3 Current'),
                new CosemRangeGroup('0', ''),
                new CosemRangeGroup('0-63', 'Rate $E (20 is total)'),
                new CosemRangeGroup('151,255', '')
        ])
        def obis = new Obis('1.48.7.0.18.151')

        expect:
        definition.getDescription(obis) == 'Electricity, Ch. 48, L3 Current, Rate 18 (20 is total)'
    }

    def "contains: should return false if it does not match obis"() {
        given:
        def definition = new CosemDefinition([
                new CosemRangeGroup('1'),
                new CosemRangeGroup('0-63'),
                new CosemRangeGroup('1-92'),
                new CosemRangeGroup('0'),
                new CosemRangeGroup('0-63'),
                new CosemRangeGroup('151,255')
        ])
        def obis = new Obis('1.48.7.0.18.200')

        expect:
        !definition.contains(obis)
    }

    def "contains: should return true if groups contain appropriate obis group id"() {
        given:
        def definition = new CosemDefinition([
                new CosemRangeGroup('1'),
                new CosemRangeGroup('0-63'),
                new CosemRangeGroup('1-92'),
                new CosemRangeGroup('0'),
                new CosemRangeGroup('0-63'),
                new CosemRangeGroup('151,255')
        ])
        def obis = new Obis('1.48.7.0.18.255')

        expect:
        definition.contains(obis)
    }
}
