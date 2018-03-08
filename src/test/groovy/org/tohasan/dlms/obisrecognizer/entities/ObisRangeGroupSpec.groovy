package org.tohasan.dlms.obisrecognizer.entities

import spock.lang.Specification

class ObisRangeGroupSpec extends Specification {
    def descriptionDictionary = [
            '$1': [
                    new ObisGroupDescription(3, 'Sum Li Reactive Power')
            ],
            '$2': [
                    new ObisGroupDescription(17, 'Time integral 7')
            ]
    ]

    def "should return true if group range contains specified group id"() {
        given:
        def groupId = 3
        def rangeGroup = new ObisRangeGroup('1-20')

        expect:
        rangeGroup.contains(groupId)
    }

    def "should return false if group range does not contain specified group id"() {
        given:
        def groupId = 3
        def rangeGroup = new ObisRangeGroup('21-40')

        expect:
        !rangeGroup.contains(groupId)
    }

    def "should return empty description if there is no specified group id in group range"() {
        given:
        def groupId = 3
        def rangeGroup = new ObisRangeGroup('21-40', '', descriptionDictionary)

        expect:
        rangeGroup.getDescription(groupId) == ''
    }

    def "should return description from description dictionary if group description contains description number"() {
        given:
        def groupId = 3
        def rangeGroup = new ObisRangeGroup('3-10', '$1', descriptionDictionary)

        expect:
        rangeGroup.getDescription(groupId) == 'Sum Li Reactive Power'
    }

    def "should return empty string if group description contains description number and there is no description in dictionary"() {
        given:
        def groupId = 3
        def rangeGroup = new ObisRangeGroup('3-10', '$2', descriptionDictionary)

        expect:
        rangeGroup.getDescription(groupId) == ''
    }

    def "should return description as is if there are no variable components in the description"() {
        given:
        def groupId = 3
        def rangeGroup = new ObisRangeGroup('3-10', 'Some description')

        expect:
        rangeGroup.getDescription(groupId) == 'Some description'
    }

    def "should replace group name (\$B, \$E etc) with group id in description"() {
        given:
        def groupId = 3
        def rangeGroup = new ObisRangeGroup('3-10', 'Some description')

        expect:
        rangeGroup.getDescription(groupId) == 'Some description'
    }

    def "should replace group id placeholder in description"() {
        given:
        def groupId = 7
        def rangeGroup = new ObisRangeGroup('3-10', 'Channel #$B')

        expect:
        rangeGroup.getDescription(groupId) == 'Channel #7'
    }

    def "should replace expression that includes group name (for instance, \$(F+10)) with expression result"() {
        given:
        def groupId = 101
        def rangeGroup = new ObisRangeGroup('0-255', '$(F-100) last billing period')

        expect:
        rangeGroup.getDescription(groupId) == '1 last billing period'
    }

    def "should replace multiple expressions in description (\$(F-100)/\$F)"() {
        given:
        def groupId = 10
        def rangeGroup = new ObisRangeGroup('0-255', '$(F-2) last / $(F+2) last billing period(s)')

        expect:
        rangeGroup.getDescription(groupId) == '8 last / 12 last billing period(s)'
    }

    def "should replace multiple identical expressions in description (\$(F-100)/\$(F-100))"() {
        given:
        def groupId = 108
        def rangeGroup = new ObisRangeGroup('0-255', '$(F-100) last / $(F-100) last billing period(s)')

        expect:
        rangeGroup.getDescription(groupId) == '8 last / 8 last billing period(s)'
    }
}
