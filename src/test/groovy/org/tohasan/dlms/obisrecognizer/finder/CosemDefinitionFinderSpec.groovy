package org.tohasan.dlms.obisrecognizer.finder

import org.tohasan.dlms.obisrecognizer.entities.CosemDefinition
import org.tohasan.dlms.obisrecognizer.entities.CosemRangeGroup
import org.tohasan.dlms.obisrecognizer.entities.Obis
import spock.lang.Specification

class CosemDefinitionFinderSpec extends Specification {
    def definitions = [
            new CosemDefinition([
                    new CosemRangeGroup('1', 'Electricity'),
                    new CosemRangeGroup('0-63', 'Ch. $B'),
                    new CosemRangeGroup('1-92', 'L3 Current'),
                    new CosemRangeGroup('0', 'Billing period avg.'),
                    new CosemRangeGroup('0-63', 'Rate $E (0 is total)'),
                    new CosemRangeGroup('151,255', 'Last billing period(s)')
            ]),
            new CosemDefinition([
                    new CosemRangeGroup('1', 'Electricity'),
                    new CosemRangeGroup('0-63', 'Ch. $B'),
                    new CosemRangeGroup('1-10', 'Sum Li Reactive Power'),
                    new CosemRangeGroup('0', 'Billing period avg.'),
                    new CosemRangeGroup('0-63', 'Rate $E (0 is total)'),
                    new CosemRangeGroup('151,255', 'Last billing period(s)')
            ])
    ]

    def "should return null if no one definition was found by specified obis"() {
        given:
        def obis = new Obis('1.2.18.0.7.150')
        def finder = new CosemDefinitionFinder(definitions)

        expect:
        finder.findDescriptionsByObis(obis) == []
    }

    def "should return description of found definition by specified obis"() {
        given:
        def obis = new Obis('1.2.18.0.7.151')
        def finder = new CosemDefinitionFinder(definitions)

        expect:
        finder.findDescriptionsByObis(obis) == [
                'Electricity, Ch. 2, L3 Current, Billing period avg., Rate 7 (0 is total), Last billing period(s)'
        ]
    }

    def "should return multiple descriptions if there are more than one definitions for specified obis"() {
        given:
        def obis = new Obis('1.2.8.0.7.151')
        def finder = new CosemDefinitionFinder(definitions)

        expect:
        finder.findDescriptionsByObis(obis) == [
                'Electricity, ' +
                        'Ch. 2, ' +
                        'L3 Current, ' +
                        'Billing period avg., ' +
                        'Rate 7 (0 is total), ' +
                        'Last billing period(s)',
                'Electricity, ' +
                        'Ch. 2, ' +
                        'Sum Li Reactive Power, ' +
                        'Billing period avg., ' +
                        'Rate 7 (0 is total), ' +
                        'Last billing period(s)'
        ]
    }
}
